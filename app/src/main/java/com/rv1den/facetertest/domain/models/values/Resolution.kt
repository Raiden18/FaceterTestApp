package com.rv1den.facetertest.domain.models.values

data class Resolution(
    val width: Int,
    val height: Int
) {
    companion object {
        fun fullHd() = Resolution(200, 200)
    }
}