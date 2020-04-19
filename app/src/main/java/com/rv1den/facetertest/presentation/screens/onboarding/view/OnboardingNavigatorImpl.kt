package com.rv1den.facetertest.presentation.screens.onboarding.view

import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import com.rv1den.facetertest.presentation.screens.camera.view.CameraActivity
import com.rv1den.facetertest.presentation.screens.onboarding.presenter.OnboardingNavigator

class OnboardingNavigatorImpl(
    private val application: Application
) : OnboardingNavigator {

    override fun openCamera() {
        val intent = Intent(application, CameraActivity::class.java).apply {
            addFlags(FLAG_ACTIVITY_SINGLE_TOP)
            addFlags(FLAG_ACTIVITY_NEW_TASK)
        }
        application.startActivity(intent)
    }
}