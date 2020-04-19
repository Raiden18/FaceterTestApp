package com.rv1den.facetertest.presentation.features.camera.presenter

import android.util.Log
import com.rv1den.facetertest.domain.models.enteties.Camera
import com.rv1den.facetertest.domain.models.values.Resolution
import com.rv1den.facetertest.domain.usecases.GetCameraUseCase
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import moxy.MvpPresenter

class CameraPresenter(
    private val getCameraUseCase: GetCameraUseCase
) : MvpPresenter<CameraView>() {

    private var defaultResolution = Resolution.fullHd()

    override fun attachView(view: CameraView?) {
        super.attachView(view)
        viewState.initCamera(defaultResolution)
    }

    fun setImageWidth(width: String) {

    }

    fun setImageHeight(height: String) {

    }

    private fun updateCameraResolution(resolution: Resolution) {

    }

    private fun showError(throwable: Throwable) {

    }
}