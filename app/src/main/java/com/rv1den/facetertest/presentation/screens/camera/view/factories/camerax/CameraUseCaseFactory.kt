package com.rv1den.facetertest.presentation.screens.camera.view.factories.camerax

import androidx.camera.core.UseCase
import androidx.camera.core.impl.ImageOutputConfig
import com.rv1den.facetertest.domain.models.Resolution

interface CameraUseCaseFactory {
    fun create(resolution: Resolution, @ImageOutputConfig.RotationValue rotation: Int): UseCase
}