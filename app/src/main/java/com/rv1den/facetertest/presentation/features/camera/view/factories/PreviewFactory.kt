package com.rv1den.facetertest.presentation.features.camera.view.factories

import android.annotation.SuppressLint
import android.util.Size
import androidx.camera.core.Preview
import com.rv1den.facetertest.domain.models.enteties.Camera
import com.rv1den.facetertest.domain.models.values.Resolution

class PreviewFactory : CameraUseCaseFactory {

    @SuppressLint("RestrictedApi")
    override fun create(resolution: Resolution, rotation: Int): Preview {
        val size = Size(resolution.width, resolution.height)
        return Preview.Builder()
            .setMaxResolution(size)
            .setTargetRotation(rotation)
            .build()
    }
}