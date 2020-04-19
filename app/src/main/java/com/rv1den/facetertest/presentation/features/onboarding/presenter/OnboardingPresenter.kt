package com.rv1den.facetertest.presentation.features.onboarding.presenter

import moxy.MvpPresenter
import kotlin.collections.HashMap

class OnboardingPresenter(
    private val permissionRequester: PermissionRequester,
    private val onboardingNavigator: OnboardingNavigator
) : MvpPresenter<OnboardingView>() {

    fun onOpenCameraClick() {
        requestPermissions()
    }

    private fun requestPermissions() {
        permissionRequester.requestPermission { resultMap ->
            checkIfPermissionsGranted(resultMap)
        }
    }

    private fun checkIfPermissionsGranted(permissionsResult: HashMap<String, Boolean>) {
        if (permissionsResult.isAllPermissionGranted()) {
            onboardingNavigator.openCamera()
        }
    }

    private fun HashMap<String, Boolean>.isAllPermissionGranted(): Boolean {
        return values.all { it }
    }
}