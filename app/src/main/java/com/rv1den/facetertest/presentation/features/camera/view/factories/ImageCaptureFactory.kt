package com.rv1den.facetertest.presentation.features.camera.view.factories

import android.annotation.SuppressLint
import android.util.Size
import androidx.camera.core.ImageCapture
import com.rv1den.facetertest.domain.models.enteties.Camera

class ImageCaptureFactory : CameraUseCaseFactory {

    @SuppressLint("RestrictedApi")
    override fun create(camera: Camera, rotation: Int): ImageCapture {
        val resolution = camera.resolution
        val size = Size(resolution.width, resolution.height)
        return ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setMaxResolution(size)
            .setTargetRotation(rotation)
            .build()
    }
}