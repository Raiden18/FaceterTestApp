package com.rv1den.facetertest.features.onboarding.view

import com.rv1den.facetertest.features.onboarding.presenter.OnboardingNavigator
import com.rv1den.facetertest.features.onboarding.presenter.OnboardingPresenter
import com.rv1den.facetertest.features.onboarding.presenter.PermissionRequester
import javax.inject.Provider

class OnboardingPresenterProvider(
    private val permissionRequester: PermissionRequester,
    private val onboardingNavigator: OnboardingNavigator
) : Provider<OnboardingPresenter> {
    override fun get(): OnboardingPresenter {
        return OnboardingPresenter(
            permissionRequester,
            onboardingNavigator
        )
    }
}