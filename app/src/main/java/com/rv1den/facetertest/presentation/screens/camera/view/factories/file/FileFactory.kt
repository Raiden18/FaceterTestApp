package com.rv1den.facetertest.presentation.screens.camera.view.factories.file

import android.content.Context
import java.io.File

interface FileFactory {
    fun create(context: Context): File
}