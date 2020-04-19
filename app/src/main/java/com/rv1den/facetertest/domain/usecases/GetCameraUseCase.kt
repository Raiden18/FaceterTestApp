package com.rv1den.facetertest.domain.usecases

import com.rv1den.facetertest.domain.models.enteties.Camera
import com.rv1den.facetertest.domain.reposiories.CameraRepository

class GetCameraUseCase(
    private val cameraRepository: CameraRepository
) {

    fun execute(): List<Camera> {
        return cameraRepository.getCameras()
    }
}