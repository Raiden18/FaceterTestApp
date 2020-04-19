package com.rv1den.facetertest.presentation.screens.camera.view.commands

import android.app.Activity
import android.app.AlertDialog
import android.media.MediaScannerConnection
import android.net.Uri
import android.util.Log
import androidx.camera.core.ImageCapture
import com.rv1den.facetertest.R
import com.rv1den.facetertest.presentation.screens.camera.view.ImageCaptureOnImageSavedCallback
import com.rv1den.facetertest.presentation.screens.camera.view.factories.file.FileFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import java.io.File
import java.util.concurrent.ExecutorService

class TakePhotoCommand(
    private val activity: Activity,
    private val imageFileFactory: FileFactory,
    private var imageCapture: ImageCapture,
    private val cameraExecutor: ExecutorService
) : Command {

    override fun execute() {
        val photoFile = imageFileFactory.create(activity)
        Single.just(photoFile)
            .map { createOutputFileOptions(it) }
            .flatMap { takePicture(it) }
            .map { createUriFile(it, photoFile) }
            .doOnSuccess { savePhotoToGallery(it.path.toString()) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ showSuccessMessage() }, ::showError)

    }

    private fun createOutputFileOptions(photoFile: File): ImageCapture.OutputFileOptions {
        return ImageCapture.OutputFileOptions.Builder(photoFile).build()
    }

    private fun createUriFile(fileResult: ImageCapture.OutputFileResults, photoFile: File): Uri {
        return fileResult.savedUri ?: Uri.fromFile(photoFile)
    }

    private fun takePicture(outputOptions: ImageCapture.OutputFileOptions): Single<ImageCapture.OutputFileResults> {
        return Single.create { emitter ->
            imageCapture.takePicture(
                outputOptions,
                cameraExecutor,
                ImageCaptureOnImageSavedCallback(emitter)
            )
        }
    }

    private fun savePhotoToGallery(photoPath: String) {
        MediaScannerConnection.scanFile(
            activity,
            arrayOf(photoPath),
            arrayOf("images/*")
        ) { _, _ -> Log.i("SAVED", photoPath) }
    }

    private fun showSuccessMessage() {
        AlertDialog.Builder(activity)
            .setMessage(R.string.success_message)
            .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
            .show()
    }


    private fun showError(throwable: Throwable) {
        Log.i("Error", throwable.message.toString())
    }
}