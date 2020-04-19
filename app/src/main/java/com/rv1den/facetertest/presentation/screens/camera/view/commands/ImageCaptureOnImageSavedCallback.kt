package com.rv1den.facetertest.presentation.screens.camera.view.commands

import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import io.reactivex.rxjava3.core.SingleEmitter

class ImageCaptureOnImageSavedCallback(
    private val singleEmitter: SingleEmitter<ImageCapture.OutputFileResults>
) : ImageCapture.OnImageSavedCallback {

    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
        singleEmitter.onSuccess(outputFileResults)
    }

    override fun onError(exception: ImageCaptureException) {
        singleEmitter.tryOnError(exception)
    }
}
