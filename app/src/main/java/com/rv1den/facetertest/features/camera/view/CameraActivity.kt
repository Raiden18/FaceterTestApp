package com.rv1den.facetertest.features.camera.view

import android.content.Intent
import android.os.Bundle
import com.livetyping.permission.PermissionBinder
import com.rv1den.facetertest.R
import com.rv1den.facetertest.app.FaceterTestApplication
import com.rv1den.facetertest.features.camera.di.DaggerCameraComponent
import com.rv1den.facetertest.features.camera.presenter.CameraPresenterProvider
import com.rv1den.facetertest.features.camera.presenter.CameraView
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject

class CameraActivity : MvpAppCompatActivity(), CameraView {
    @Inject
    lateinit var presenterProvider: CameraPresenterProvider

    private val presenter by moxyPresenter {
        presenterProvider.get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initDagger()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
    }

    private fun initDagger() {
        val projectComponent = (application as FaceterTestApplication).projectComponent
        DaggerCameraComponent.builder()
            .projectComponent(projectComponent)
            .build()
            .inject(this)
    }

}
