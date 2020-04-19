package com.rv1den.facetertest.presentation.features.camera.view.factories

import androidx.camera.core.UseCase
import androidx.camera.core.impl.ImageOutputConfig
import com.rv1den.facetertest.domain.models.enteties.Camera
import com.rv1den.facetertest.domain.models.values.Resolution

interface CameraUseCaseFactory {
    fun create(resolution: Resolution, @ImageOutputConfig.RotationValue rotation: Int): UseCase
}