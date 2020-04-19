package com.rv1den.facetertest.presentation.screens.onboarding.presenter

interface PermissionRequester {
    fun requestPermission(resultListener: (HashMap<String, Boolean>) -> Unit)
}