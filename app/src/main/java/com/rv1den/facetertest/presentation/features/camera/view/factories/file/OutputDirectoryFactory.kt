package com.rv1den.facetertest.presentation.features.camera.view.factories.file

import android.content.Context
import com.rv1den.facetertest.R
import java.io.File

class OutputDirectoryFactory : FileFactory {
    override fun create(context: Context): File {
        val appContext = context.applicationContext
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else appContext.filesDir
    }
}