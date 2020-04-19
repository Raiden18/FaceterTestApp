package com.rv1den.facetertest.presentation.screens.camera.view

import android.content.Context
import android.view.OrientationEventListener
import android.view.Surface
import androidx.camera.core.ImageCapture

class OrientationEventListenerImpl(
    context: Context,
    private var imageCapture: ImageCapture
) : OrientationEventListener(context) {
    private companion object {
        const val ROTATION_O = 1
        const val ROTATION_90 = 2
        const val ROTATION_180 = 3
        const val ROTATION_270 = 4
    }

    private var currentRotation = 0

    override fun onOrientationChanged(orientation: Int) {
        currentRotation = calculateCurrentAngleRotation(orientation)
        imageCapture.targetRotation = currentRotation
    }

    private fun calculateCurrentAngleRotation(orientation: Int): Int {
        return when {
            (orientation < 35 || orientation > 325) && currentRotation != ROTATION_O -> Surface.ROTATION_0
            orientation in 146..214 && currentRotation != ROTATION_180 -> Surface.ROTATION_180
            orientation in 56..124 && currentRotation != ROTATION_270 -> Surface.ROTATION_270
            orientation in 236..304 && currentRotation != ROTATION_90 -> Surface.ROTATION_90
            else -> Surface.ROTATION_0
        }
    }
}