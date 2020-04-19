package com.rv1den.facetertest.presentation.features.onboarding.view

import android.content.Intent
import android.os.Bundle
import com.livetyping.permission.PermissionBinder
import com.rv1den.facetertest.R
import com.rv1den.facetertest.presentation.app.FaceterTestApplication
import com.rv1den.facetertest.presentation.features.onboarding.di.DaggerOnboardingComponent
import com.rv1den.facetertest.presentation.features.onboarding.presenter.OnboardingView
import kotlinx.android.synthetic.main.activity_onboarding.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject

class OnboardingActivity : MvpAppCompatActivity(), OnboardingView {
    @Inject
    lateinit var permissionBinder: PermissionBinder
    @Inject
    lateinit var presenterProvider: OnboardingPresenterProvider

    private val presenter by moxyPresenter { presenterProvider.get() }

    override fun onCreate(savedInstanceState: Bundle?) {
        val projectComponent = (application as FaceterTestApplication).projectComponent
        val activityComponent = DaggerOnboardingComponent.builder()
            .projectComponent(projectComponent)
            .build()
        activityComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        onboarding_open_camera_button.setOnClickListener {
            presenter.onOpenCameraClick()
        }
    }

    override fun onStart() {
        super.onStart()
        permissionBinder.attach(this)
    }

    override fun onStop() {
        permissionBinder.detach(this)
        super.onStop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        permissionBinder.onActivityResult(requestCode, data, this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionBinder.onRequestPermissionResult(requestCode, grantResults)
    }
}