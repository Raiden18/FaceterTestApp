package com.rv1den.facetertest.presentation.features.camera.view.factories

import android.annotation.SuppressLint
import android.util.Size
import androidx.camera.core.ImageCapture
import com.rv1den.facetertest.domain.models.enteties.Camera
import com.rv1den.facetertest.domain.models.values.Resolution

class ImageCaptureFactory : CameraUseCaseFactory {

    @SuppressLint("RestrictedApi")
    override fun create(resolution: Resolution, rotation: Int): ImageCapture {
        val size = Size(resolution.width, resolution.height)
        return ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setMaxResolution(size)
            .setTargetRotation(rotation)
            .build()
    }
}