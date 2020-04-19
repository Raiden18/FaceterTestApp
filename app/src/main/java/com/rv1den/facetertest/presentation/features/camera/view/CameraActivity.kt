package com.rv1den.facetertest.presentation.features.camera.view

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.CameraManager
import android.hardware.display.DisplayManager
import android.os.Bundle
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.rv1den.facetertest.R
import com.rv1den.facetertest.domain.models.enteties.Camera
import com.rv1den.facetertest.domain.models.values.Resolution
import com.rv1den.facetertest.presentation.app.FaceterTestApplication
import com.rv1den.facetertest.presentation.features.camera.di.DaggerCameraComponent
import com.rv1den.facetertest.presentation.features.camera.presenter.CameraPresenterProvider
import com.rv1den.facetertest.presentation.features.camera.presenter.CameraView
import com.rv1den.facetertest.presentation.features.camera.view.factories.ImageCaptureFactory
import com.rv1den.facetertest.presentation.features.camera.view.factories.PreviewFactory
import kotlinx.android.synthetic.main.activity_camera.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class CameraActivity : MvpAppCompatActivity(), CameraView {
    companion object {

        /** Use external media if it is available, our app's file directory otherwise */
        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
            }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }
    }

    @Inject
    lateinit var presenterProvider: CameraPresenterProvider
    @Inject
    lateinit var cameraManager: CameraManager
    private val presenter by moxyPresenter {
        presenterProvider.get()
    }

    private var container: ConstraintLayout? = null
    private var viewFinder: PreviewView? = null
    private lateinit var outputDirectory: File

    private var displayId: Int = -1
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: androidx.camera.core.Camera? = null

    private val displayManager by lazy {
        getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    private lateinit var cameraExecutor: ExecutorService
    private val imageCaptureFactory = ImageCaptureFactory()
    private val previewFactory = PreviewFactory()

    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = Unit
        override fun onDisplayRemoved(displayId: Int) = Unit
        override fun onDisplayChanged(displayId: Int) = container?.let { view ->
            if (displayId == this@CameraActivity.displayId) {
                imageCapture?.targetRotation = view.display.rotation
                imageAnalyzer?.targetRotation = view.display.rotation
            }
        } ?: Unit
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        displayManager.unregisterDisplayListener(displayListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initDagger()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        container = camera_container as ConstraintLayout
        viewFinder = view_finder
        cameraExecutor = Executors.newSingleThreadExecutor()
        displayManager.registerDisplayListener(displayListener, null)
        outputDirectory = getOutputDirectory(this)
    }

    private fun bindCameraUseCases(resolution: Resolution) {
        val rotation = viewFinder!!.display.rotation
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        val mainExecutor = ContextCompat.getMainExecutor(this@CameraActivity)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            preview = previewFactory.create(resolution, rotation)
            imageCapture = imageCaptureFactory.create(resolution, rotation)
            // Must unbind the use-cases before rebinding them
            cameraProvider.unbindAll()

            this.camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture
            )
            val cameraInfo = this.camera?.cameraInfo
            val surfaceProvider = viewFinder!!.createSurfaceProvider(cameraInfo)
            preview?.setSurfaceProvider(surfaceProvider)
        }, mainExecutor)
    }


    override fun initCamera(resolution: Resolution) {
        viewFinder?.post {
            displayId = viewFinder!!.display.displayId
            bindCameraUseCases(resolution)
        }
    }

    private fun initDagger() {
        val projectComponent = (application as FaceterTestApplication).projectComponent
        DaggerCameraComponent.builder()
            .projectComponent(projectComponent)
            .build()
            .inject(this)
    }
}
