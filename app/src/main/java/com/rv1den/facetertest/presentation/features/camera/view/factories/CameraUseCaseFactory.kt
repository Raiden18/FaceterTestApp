package com.rv1den.facetertest.presentation.features.camera.view.factories

import androidx.camera.core.UseCase
import androidx.camera.core.impl.ImageOutputConfig
import com.rv1den.facetertest.domain.models.enteties.Camera

interface CameraUseCaseFactory {
    fun create(camera: Camera, @ImageOutputConfig.RotationValue rotation: Int): UseCase
}