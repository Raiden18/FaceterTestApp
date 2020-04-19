package com.rv1den.facetertest.presentation.features.camera.view

import android.content.Context
import android.hardware.camera2.CameraManager
import android.hardware.display.DisplayManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.OrientationEventListener
import android.view.OrientationListener
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.rv1den.facetertest.R
import com.rv1den.facetertest.domain.models.values.Resolution
import com.rv1den.facetertest.presentation.app.FaceterTestApplication
import com.rv1den.facetertest.presentation.features.camera.di.DaggerCameraComponent
import com.rv1den.facetertest.presentation.features.camera.presenter.CameraPresenterProvider
import com.rv1den.facetertest.presentation.features.camera.presenter.CameraView
import com.rv1den.facetertest.presentation.features.camera.view.commands.TakePhotoCommand
import com.rv1den.facetertest.presentation.features.camera.view.factories.camerax.ImageCaptureFactory
import com.rv1den.facetertest.presentation.features.camera.view.factories.camerax.PreviewFactory
import com.rv1den.facetertest.presentation.features.camera.view.factories.file.ImageFileFactory
import com.rv1den.facetertest.presentation.features.camera.view.factories.file.OutputDirectoryFactory
import com.rv1den.facetertest.presentation.features.camera.view.settings.BottomSheetSettings
import io.reactivex.rxjava3.core.Single
import kotlinx.android.synthetic.main.activity_camera.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.io.File
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class CameraActivity : MvpAppCompatActivity(), CameraView {
    @Inject
    lateinit var presenterProvider: CameraPresenterProvider
    @Inject
    lateinit var cameraManager: CameraManager
    private val presenter by moxyPresenter {
        presenterProvider.get()
    }

    private var container: ConstraintLayout? = null

    private var displayId: Int = -1
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null

    private val displayManager by lazy {
        getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    private lateinit var cameraExecutor: ExecutorService
    private val imageCaptureFactory = ImageCaptureFactory()
    private val previewFactory = PreviewFactory()
    private val imageFileFactory = ImageFileFactory(OutputDirectoryFactory())

    private var orientationEventListener: OrientationEventListener? = null


    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onStop() {
        orientationEventListener?.disable()
        super.onStop()
    }

    override fun onStart() {
        orientationEventListener?.enable()
        super.onStart()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        initDagger()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        container = camera_container as ConstraintLayout
        cameraExecutor = Executors.newSingleThreadExecutor()
        settings_button.setOnClickListener {
            presenter.onSettingsClick()
        }
        camera_capture_button.setOnClickListener {
            takePhoto()
        }
    }

    private fun takePhoto() {
        TakePhotoCommand(
            this,
            imageFileFactory,
            imageCapture!!,
            cameraExecutor
        ).execute()
    }


    private fun bindCameraUseCases(resolution: Resolution) {
        val rotation = view_finder.display.rotation
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        val mainExecutor = ContextCompat.getMainExecutor(this@CameraActivity)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            preview = previewFactory.create(resolution, rotation)
            imageCapture = imageCaptureFactory.create(resolution, rotation)
            orientationEventListener = OrientationEventListenerImpl(this, imageCapture!!)
            orientationEventListener?.enable()
            // Must unbind the use-cases before rebinding them
            cameraProvider.unbindAll()
            try {
                this.camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
                val cameraInfo = this.camera?.cameraInfo
                val surfaceProvider = view_finder.createSurfaceProvider(cameraInfo)
                preview?.setSurfaceProvider(surfaceProvider)
            } catch (exception: IllegalArgumentException) {
                exception.printStackTrace()
            }

        }, mainExecutor)
    }

    override fun showSettings(resolution: Resolution) {
        val bottomSheetSettings = BottomSheetSettings()
        bottomSheetSettings.heightChangeListener = presenter::setImageHeight
        bottomSheetSettings.widthChangeListener = presenter::setImageWidth
        bottomSheetSettings.onApplyClickListener = presenter::applySettings
        bottomSheetSettings.resolution = resolution
        bottomSheetSettings.show(supportFragmentManager, "TAG")
    }

    override fun initCamera(resolution: Resolution) {
        view_finder.post {
            displayId = view_finder.display.displayId
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
