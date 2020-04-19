package com.rv1den.facetertest.presentation.screens.camera.view

import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import io.reactivex.rxjava3.core.SingleEmitter

class ImageCaptureOnImageSavedCallback(
    private val singleEmitter: SingleEmitter<ImageCapture.OutputFileResults>
) : ImageCapture.OnImageSavedCallback {

    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
        singleEmitter.onSuccess(outputFileResults)
        /* val savedUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)
        savePhotoToGallery(savedUri.path.toString())*/
    }

    override fun onError(exception: ImageCaptureException) {
        singleEmitter.tryOnError(exception)
    }
}
 /*   private fun savePhotoToGallery(photoPath: String) {}
      *//*  MediaScannerConnection.scanFile(
            context,
            arrayOf(photoPath),
            arrayOf("images/*")
        ) { _, _ -> Log.i("SAVED", photoPath) }
        Toast.makeText(context, "Image saved to Gallery", Toast.LENGTH_LONG).show()*//*
    }
*/