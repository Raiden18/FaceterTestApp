package com.rv1den.facetertest.presentation.screens.camera.presenter

import com.rv1den.facetertest.domain.models.Resolution
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEnd

interface CameraView : MvpView {
    @AddToEnd
    fun initCamera(resolution: Resolution)

    @AddToEnd
    fun showSettings(resolution: Resolution)
}