package com.rv1den.facetertest.presentation.features.camera.view.factories.file

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ImageFileFactory(
    private val outputDirectoryFactory: OutputDirectoryFactory
) : FileFactory {
    private companion object {
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
    }

    override fun create(context: Context): File {
        val baseFolder = outputDirectoryFactory.create(context)
        val dateFormat = SimpleDateFormat(FILENAME, Locale.US)
        val date = dateFormat.format(System.currentTimeMillis())
        val fileName = date + PHOTO_EXTENSION
        return File(baseFolder, fileName)
    }
}