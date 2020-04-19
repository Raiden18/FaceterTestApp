package com.rv1den.facetertest.data.repositories

import android.graphics.ImageFormat
import android.hardware.camera2.CameraManager
import com.rv1den.facetertest.data.models.CameraCharacteristics
import com.rv1den.facetertest.domain.models.enteties.Camera
import com.rv1den.facetertest.domain.models.values.Resolution

class CamerasFetcher(
    private val cameraManager: CameraManager
) {
    private var availableCameras: MutableList<Camera> = mutableListOf()
    private lateinit var cameraIds: List<String>
    private val fullHdResolution = Resolution.fullHd()

    fun fetch(): List<Camera> {
        availableCameras.clear()
        findAllCompatibleCamerasIds()
        cameraIds.forEach { id -> findCompatibleCameras(id) }
        return availableCameras
    }

    private fun findAllCompatibleCamerasIds() {
        this.cameraIds = cameraManager.cameraIdList.filter {
            val androidCharacteristics = cameraManager.getCameraCharacteristics(it)
            val cameraCharacteristics = CameraCharacteristics(androidCharacteristics)
            cameraCharacteristics.isBackwardCompatible
        }
    }

    private fun findCompatibleCameras(id: String) {
        val androidCharacteristics = cameraManager.getCameraCharacteristics(id)
        val cameraCharacteristics = CameraCharacteristics(androidCharacteristics)
        addJpegCamera(id)
        if (cameraCharacteristics.isCapableForRaw && cameraCharacteristics.isOutputRawSensor) {
            addRawCamera(id)
        }
        if (cameraCharacteristics.isCapableDepthOutPut && cameraCharacteristics.isCapableDepthJpeg) {
            addDepthCamera(id)
        }
    }

    private fun addJpegCamera(id: String) {
        val jpegCamera = Camera(id, ImageFormat.JPEG, fullHdResolution)
        addCamera(jpegCamera)
    }

    private fun addRawCamera(id: String) {
        val rawCamera = Camera(id, ImageFormat.RAW_SENSOR, fullHdResolution)
        addCamera(rawCamera)
    }

    private fun addDepthCamera(id: String) {
        val depthCamera = Camera(id, ImageFormat.DEPTH_JPEG, fullHdResolution)
        addCamera(depthCamera)
    }

    private fun addCamera(camera: Camera) {
        availableCameras.add(camera)
    }
}