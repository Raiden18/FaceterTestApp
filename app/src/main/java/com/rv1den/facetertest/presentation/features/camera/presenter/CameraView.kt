package com.rv1den.facetertest.presentation.features.camera.presenter

import com.rv1den.facetertest.domain.models.enteties.Camera
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEnd

interface CameraView : MvpView {
    @AddToEnd
    fun initCamera(camera: Camera)
}