package com.rv1den.facetertest.domain.reposiories

import com.rv1den.facetertest.domain.models.enteties.Camera

interface CameraRepository {
    fun getCameras(): List<Camera>
}