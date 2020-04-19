package com.rv1den.facetertest.presentation.screens.camera.presenter

import javax.inject.Provider

class CameraPresenterProvider: Provider<CameraPresenter> {
    override fun get(): CameraPresenter {
        return CameraPresenter()
    }
}