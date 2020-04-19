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

    private lateinit var backCamera: Camera

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        val cameras = getCameraUseCase.execute()
        backCamera = cameras.first()
    }

    override fun attachView(view: CameraView?) {
        super.attachView(view)
        viewState.initCamera(backCamera)
    }

    fun setImageWidth(width: String) {
        Observable.just(width)
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
            .map { backCamera.resolution.copy(width = it) }
            .subscribe(::updateCameraResolution, ::showError)
    }

    fun setImageHeight(height: String) {
        Single.just(height)
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
            .map { backCamera.resolution.copy(height = it) }
            .subscribe(::updateCameraResolution, ::showError)
    }

    private fun updateCameraResolution(resolution: Resolution) {
        backCamera = backCamera.copy(resolution = resolution)
        viewState.initCamera(backCamera)
    }

    private fun showError(throwable: Throwable) {
        Log.i("ERROR", throwable.message)
    }
}