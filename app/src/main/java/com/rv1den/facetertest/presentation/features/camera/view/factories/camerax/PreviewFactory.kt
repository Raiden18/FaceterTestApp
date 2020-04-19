package com.rv1den.facetertest.presentation.features.camera.view.factories.camerax

import android.annotation.SuppressLint
import android.util.Size
import androidx.camera.core.Preview
import com.rv1den.facetertest.domain.models.values.Resolution
import com.rv1den.facetertest.presentation.features.camera.view.factories.camerax.CameraUseCaseFactory

class PreviewFactory :
    CameraUseCaseFactory {

    @SuppressLint("RestrictedApi")
    override fun create(resolution: Resolution, rotation: Int): Preview {
        val size = Size(resolution.width, resolution.height)
        return Preview.Builder()
            .setMaxResolution(size)
            .setTargetRotation(rotation)
            .build()
    }
}