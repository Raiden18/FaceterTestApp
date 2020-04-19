package com.rv1den.facetertest.domain.models.enteties

import com.rv1den.facetertest.domain.models.values.Resolution

data class Camera(
    val id: String,
    val format: Int,
    val resolution: Resolution,
    val rotation: Int = -1
)