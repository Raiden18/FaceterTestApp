package com.rv1den.facetertest.presentation.screens.onboarding.view

import com.rv1den.facetertest.presentation.screens.onboarding.presenter.OnboardingNavigator
import com.rv1den.facetertest.presentation.screens.onboarding.presenter.OnboardingPresenter
import com.rv1den.facetertest.presentation.screens.onboarding.presenter.PermissionRequester
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