package com.rv1den.facetertest.domain.models

data class Resolution(
    val width: Int,
    val height: Int
) {
    companion object {
        fun fullHd() =
            Resolution(1920, 1080)
    }
}