package com.rv1den.facetertest.presentation.screens.camera.presenter

import com.rv1den.facetertest.domain.models.Resolution
import moxy.MvpPresenter

class CameraPresenter : MvpPresenter<CameraView>() {
    private var resolution = Resolution.fullHd()


    override fun attachView(view: CameraView?) {
        super.attachView(view)
        viewState.initCamera(resolution)
    }

    fun setImageWidth(width: String) {
        if (width.isNotEmpty()) {
            resolution = resolution.copy(width = width.toInt())
        }
    }

    fun setImageHeight(height: String) {
        if (height.isNotEmpty()) {
            resolution = resolution.copy(height = height.toInt())
        }
    }

    fun onSettingsClick() {
        viewState.showSettings(resolution)
    }

    fun applySettings() {
        viewState.initCamera(resolution)
    }
}