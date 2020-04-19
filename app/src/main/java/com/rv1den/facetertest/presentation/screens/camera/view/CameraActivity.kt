package com.rv1den.facetertest.presentation.screens.camera.view

import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.OrientationEventListener
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.rv1den.facetertest.R
import com.rv1den.facetertest.domain.models.Resolution
import com.rv1den.facetertest.presentation.app.FaceterTestApplication
import com.rv1den.facetertest.presentation.screens.camera.di.DaggerCameraComponent
import com.rv1den.facetertest.presentation.screens.camera.presenter.CameraPresenterProvider
import com.rv1den.facetertest.presentation.screens.camera.presenter.CameraView
import com.rv1den.facetertest.presentation.screens.camera.view.commands.BindCameraCommand
import com.rv1den.facetertest.presentation.screens.camera.view.commands.TakePhotoCommand
import com.rv1den.facetertest.presentation.screens.camera.view.factories.bottomsheet.BottomSheetSettingsFactory
import com.rv1den.facetertest.presentation.screens.camera.view.factories.camerax.BackCameraFactory
import com.rv1den.facetertest.presentation.screens.camera.view.factories.camerax.ImageCaptureFactory
import com.rv1den.facetertest.presentation.screens.camera.view.factories.camerax.PreviewFactory
import com.rv1den.facetertest.presentation.screens.camera.view.factories.file.ImageFileFactory
import com.rv1den.facetertest.presentation.screens.camera.view.settings.BottomSheetSettings
import kotlinx.android.synthetic.main.activity_camera.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.lang.IllegalArgumentException
import java.util.concurrent.ExecutorService
import javax.inject.Inject

class CameraActivity : MvpAppCompatActivity(), CameraView {
    @Inject
    lateinit var presenterProvider: CameraPresenterProvider
    @Inject
    lateinit var cameraManager: CameraManager
    @Inject
    lateinit var imageCaptureFactory: ImageCaptureFactory
    @Inject
    lateinit var previewFactory: PreviewFactory
    @Inject
    lateinit var imageFileFactory: ImageFileFactory
    @Inject
    lateinit var backCameraFactory: BackCameraFactory
    @Inject
    lateinit var cameraExecutor: ExecutorService
    @Inject
    lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    @Inject
    lateinit var bottomSheetSettingsFactory: BottomSheetSettingsFactory

    private val presenter by moxyPresenter {
        presenterProvider.get()
    }

    var imageCapture: ImageCapture? = null
    var orientationEventListener: OrientationEventListener? = null


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

    override fun showSettings(resolution: Resolution) {
        val bottomSheet = bottomSheetSettingsFactory.create(presenter, resolution)
        bottomSheet.show(supportFragmentManager, "TAG")
    }

    override fun initCamera(resolution: Resolution) {
        view_finder.post {
            BindCameraCommand(this, resolution).execute()
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
