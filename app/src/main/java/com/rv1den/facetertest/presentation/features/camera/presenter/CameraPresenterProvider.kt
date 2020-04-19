package com.rv1den.facetertest.presentation.features.camera.presenter

import com.rv1den.facetertest.domain.usecases.GetCameraUseCase
import javax.inject.Provider

class CameraPresenterProvider(
    private val getCameraUseCase: GetCameraUseCase
) : Provider<CameraPresenter> {
    override fun get(): CameraPresenter {
        return CameraPresenter(getCameraUseCase)
    }
}