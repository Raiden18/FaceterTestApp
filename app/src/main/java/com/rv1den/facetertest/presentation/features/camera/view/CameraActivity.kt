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
import android.view.TextureView
import android.view.View
import androidx.camera.core.CameraX
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
@SuppressLint("RestrictedApi")
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

    override fun initCamera(camera: Camera) {
        CameraX.unbindAll()

        val preview = createPreviewUseCase()
        preview.setOnCapturedPointerListener { view, event ->  }
    }

    private fun createPreviewUseCase(): TextureView {

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
