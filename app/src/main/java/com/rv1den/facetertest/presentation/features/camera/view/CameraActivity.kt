package com.rv1den.facetertest.presentation.features.camera.view

import android.annotation.SuppressLint
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.Image
import android.media.ImageReader
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Surface
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import com.jakewharton.rxbinding3.widget.textChanges
import com.rv1den.facetertest.R
import com.rv1den.facetertest.domain.models.enteties.Camera
import com.rv1den.facetertest.presentation.app.FaceterTestApplication
import com.rv1den.facetertest.presentation.features.camera.di.DaggerCameraComponent
import com.rv1den.facetertest.presentation.features.camera.presenter.CameraPresenterProvider
import com.rv1den.facetertest.presentation.features.camera.presenter.CameraView
import com.rv1den.facetertest.presentation.features.camera.view.callbacks.rx.UiRxFactories
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import kotlinx.android.synthetic.main.activity_camera.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class CameraActivity : MvpAppCompatActivity(), CameraView {
    private val IMAGE_BUFFER_SIZE: Int = 3
    private val IMAGE_CAPTURE_TIMEOUT_MILLIS: Long = 5000
    @Inject
    lateinit var presenterProvider: CameraPresenterProvider
    @Inject
    lateinit var cameraManager: CameraManager

    private lateinit var characteristics: CameraCharacteristics

    private lateinit var imageReader: ImageReader
    private var viewFinder: AutoFitSurfaceView? = null

    private val cameraThread = HandlerThread("CameraThread").apply { start() }
    private val cameraHandler = Handler(cameraThread.looper)
    private val imageReaderThread = HandlerThread("imageReaderThread").apply { start() }
    private val imageReaderHandler = Handler(imageReaderThread.looper)
    private lateinit var camera: CameraDevice
    private lateinit var session: CameraCaptureSession
    private lateinit var relativeOrientation: OrientationLiveData
    private lateinit var combinedCaptureResult: CombinedCaptureResult

    private val presenter by moxyPresenter {
        presenterProvider.get()
    }

    @SuppressLint("CheckResult") // There are no possible memory leaks for RxBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        initDagger()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        photo_width.textChanges()
            .map { it.toString() }
            .distinctUntilChanged()
            .debounce(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(presenter::setImageWidth)
        photo_height.textChanges()
            .map { it.toString() }
            .distinctUntilChanged()
            .debounce(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(presenter::setImageHeight)
        take_photo_button.setOnClickListener {
            takePhoto()
        }

    }

    private fun clearImageReaderCache(imageReader: ImageReader) {
        while (imageReader.acquireNextImage() != null) {
        }
    }

    private fun startNewImageQueue(imageReader: ImageReader) {
        val imageQueue = ArrayBlockingQueue<Image>(IMAGE_BUFFER_SIZE)
        imageReader.setOnImageAvailableListener({ reader ->
            val image = reader.acquireNextImage()
            imageQueue.add(image)
        }, imageReaderHandler)
    }

    private fun createCapture(imageReader: ImageReader): Single<CaptureRequest>{
        val captureRequestBuilder =
            session.device.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
        captureRequestBuilder.addTarget(imageReader.surface)
        val captureRequest =captureRequestBuilder.build()
        return Single.just(captureRequest)
    }

    private fun takePhoto() {
        Single.just(imageReader)
            .doOnSuccess { clearImageReaderCache(it) }
            .doOnSuccess { startNewImageQueue(it) }
            .flatMap { createCapture(it) }





        session.capture(captureRequest, object : CameraCaptureSession.CaptureCallback() {
            override fun onCaptureCompleted(
                session: CameraCaptureSession,
                request: CaptureRequest,
                result: TotalCaptureResult
            ) {
                super.onCaptureCompleted(session, request, result)

                val resultTimestamp = result.get(CaptureResult.SENSOR_TIMESTAMP)

                // Set a timeout in case image captured is dropped from the pipeline
                val exc = TimeoutException("Image dequeuing took too long")
                val timeoutRunnable = Runnable { throw exc }
                imageReaderHandler.postDelayed(timeoutRunnable, IMAGE_CAPTURE_TIMEOUT_MILLIS)

                // Loop in the coroutine's context until an image with matching timestamp comes
                // We need to launch the coroutine context again because the callback is done in
                //  the handler provided to the `capture` method, not in our coroutine context

                // Dequeue images while timestamps don't match
                val image = imageQueue.take()
                // TODO(owahltinez): b/142011420
                // if (image.timestamp != resultTimestamp) continue
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                    image.format != ImageFormat.DEPTH_JPEG &&
                    image.timestamp != resultTimestamp
                ) continue
                Log.d(TAG, "Matching image dequeued: ${image.timestamp}")

                // Unset the image reader listener
                imageReaderHandler.removeCallbacks(timeoutRunnable)
                imageReader.setOnImageAvailableListener(null, null)

                // Clear the queue of images, if there are left
                while (imageQueue.size > 0) {
                    imageQueue.take().close()
                }

                // Compute EXIF orientation metadata
                val rotation = relativeOrientation.value ?: 0
                val mirrored = characteristics.get(CameraCharacteristics.LENS_FACING) ==
                        CameraCharacteristics.LENS_FACING_FRONT
                val exifOrientation = computeExifOrientation(rotation, mirrored)

                // Build the result and resume progress
                combinedCaptureResult = CombinedCaptureResult(
                    image, result, exifOrientation, imageReader.imageFormat
                )
                // There is no need to break out of the loop, this coroutine will suspend

            }
        }, cameraHandler)
    }

    override fun initCamera(camera: Camera) {
        viewFinder = AutoFitSurfaceView(this)
        photo_width.setText(camera.resolution.width.toString())
        photo_height.setText(camera.resolution.height.toString())
        val lastView = root_view.children.last()
        if (lastView is AutoFitSurfaceView) {
            root_view.removeView(lastView)
        }
        root_view.addView(viewFinder)
        characteristics = cameraManager.getCameraCharacteristics(camera.id)
        Single.just(viewFinder!!)
            .flatMap { UiRxFactories.addSurfaceHolderCallback(it, characteristics, camera) }
            .flatMap { UiRxFactories.openCamera(cameraManager, camera, cameraHandler) }
            .doOnSuccess { this@CameraActivity.camera = it }
            .flatMap { UiRxFactories.createImageReader(characteristics, camera) }
            .doOnSuccess { this@CameraActivity.imageReader = it }
            .flatMap { UiRxFactories.createSurfacesForOutputFrames(it, viewFinder!!) }
            .flatMap { UiRxFactories.createCaptureSession(this@CameraActivity.camera, it) }
            .doOnSuccess { this@CameraActivity.session = it }
            .doOnSuccess { configureSession() }
            .subscribe({}, {})
        relativeOrientation = OrientationLiveData(this, characteristics)
    }

    private fun configureSession() {
        val captureRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        captureRequestBuilder.addTarget(viewFinder!!.holder.surface)
        val captureRequest = captureRequestBuilder.build()
        session.setRepeatingRequest(captureRequest, null, cameraHandler)
    }

    override fun onStop() {
        super.onStop()
        camera.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraThread.quitSafely()
        imageReaderThread.quitSafely()
    }

    private fun initDagger() {
        val projectComponent = (application as FaceterTestApplication).projectComponent
        DaggerCameraComponent.builder()
            .projectComponent(projectComponent)
            .build()
            .inject(this)
    }

}
