package com.rv1den.facetertest.features.camera.presenter

import javax.inject.Provider

class CameraPresenterProvider : Provider<CameraPresenter> {
    override fun get(): CameraPresenter {
        return CameraPresenter()
    }
}