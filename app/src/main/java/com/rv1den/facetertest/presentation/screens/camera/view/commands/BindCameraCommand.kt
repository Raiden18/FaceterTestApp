package com.rv1den.facetertest.presentation.screens.camera.view.commands

import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.rv1den.facetertest.domain.models.Resolution
import com.rv1den.facetertest.presentation.screens.camera.view.CameraActivity
import com.rv1den.facetertest.presentation.screens.camera.view.OrientationEventListenerImpl
import kotlinx.android.synthetic.main.activity_camera.*
import java.lang.IllegalArgumentException

class BindCameraCommand(
    private val cameraActivity: CameraActivity,
    private val resolution: Resolution
) : Command {

    override fun execute() = with(cameraActivity) {
        val rotation = view_finder.display.rotation
        val cameraSelector = backCameraFactory.create()
        val mainExecutor = ContextCompat.getMainExecutor(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = previewFactory.create(resolution, rotation)
            imageCapture = imageCaptureFactory.create(resolution, rotation)
            orientationEventListener = OrientationEventListenerImpl(this, imageCapture!!)
            orientationEventListener?.enable()
            // Must unbind the use-cases before rebinding them
            cameraProvider.unbindAll()
            try {
                val camera = cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
                val cameraInfo = camera.cameraInfo
                val surfaceProvider = view_finder.createSurfaceProvider(cameraInfo)
                preview.setSurfaceProvider(surfaceProvider)
            } catch (exception: IllegalArgumentException) {
                exception.printStackTrace()
            }
        }, mainExecutor)

    }
}