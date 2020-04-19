package com.rv1den.facetertest.presentation.features.onboarding.view

import android.Manifest
import android.app.Application
import com.livetyping.permission.PermissionBinder
import com.rv1den.facetertest.R
import com.rv1den.facetertest.presentation.features.onboarding.presenter.PermissionRequester

class PermissionRequesterImpl(
    private val permissionBinder: PermissionBinder,
    private val application: Application
) : PermissionRequester {

    //TODO: add request permissions
    override fun requestPermission(resultListener: (HashMap<String, Boolean>) -> Unit) {
        val permissions = listOf(
            Manifest.permission.CAMERA
        )
        val description = application.getString(R.string.permissions_descriptions)
        val settingsButtonText = application.getString(R.string.setting_button)
        permissionBinder.activePermission(
            permissions,
            description,
            settingsButtonText,
            resultListener
        )
    }
}