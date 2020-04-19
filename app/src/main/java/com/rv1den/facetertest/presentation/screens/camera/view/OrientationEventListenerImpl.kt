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

    private var rotation = 0

    override fun onOrientationChanged(orientation: Int) {
        if ((orientation < 35 || orientation > 325) && rotation != ROTATION_O) { // PORTRAIT
            rotation = Surface.ROTATION_0
        } else if (orientation in 146..214 && rotation != ROTATION_180) { // REVERSE PORTRAIT
            rotation = Surface.ROTATION_180;
        } else if (orientation in 56..124 && rotation != ROTATION_270) { // REVERSE LANDSCAPE
            rotation = Surface.ROTATION_270;
        } else if (orientation in 236..304 && rotation != ROTATION_90) { //LANDSCAPE
            rotation = Surface.ROTATION_90;
        }
        imageCapture.targetRotation = rotation
    }
}