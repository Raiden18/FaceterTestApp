package com.rv1den.facetertest.presentation.features.camera.presenter

import com.rv1den.facetertest.domain.models.values.Resolution
import com.rv1den.facetertest.domain.usecases.GetCameraUseCase
import moxy.MvpPresenter

class CameraPresenter(
    private val getCameraUseCase: GetCameraUseCase
) : MvpPresenter<CameraView>() {
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