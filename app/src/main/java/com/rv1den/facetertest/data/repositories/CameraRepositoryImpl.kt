package com.rv1den.facetertest.data.repositories

import android.hardware.camera2.CameraManager
import com.rv1den.facetertest.domain.models.enteties.Camera
import com.rv1den.facetertest.domain.reposiories.CameraRepository

class CameraRepositoryImpl(
    cameraManager: CameraManager
) : CameraRepository {
    private val camerasFetcher = CamerasFetcher(cameraManager)

    override fun getCameras(): List<Camera> {
        return camerasFetcher.fetch()
    }
}