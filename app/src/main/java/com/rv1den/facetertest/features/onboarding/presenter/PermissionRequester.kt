package com.rv1den.facetertest.features.onboarding.presenter

interface PermissionRequester {
    fun requestPermission(resultListener: (HashMap<String, Boolean>) -> Unit)
}