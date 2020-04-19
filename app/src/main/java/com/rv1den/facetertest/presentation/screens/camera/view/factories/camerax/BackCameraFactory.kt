package com.rv1den.facetertest.presentation.screens.camera.view.factories.camerax

import androidx.camera.core.CameraSelector

class BackCameraFactory {
    fun create(): CameraSelector {
        return CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
    }
}