package com.rv1den.facetertest.presentation.screens.onboarding.di

import com.rv1den.facetertest.presentation.app.ProjectComponent
import com.rv1den.facetertest.presentation.screens.di.ActivityScope
import com.rv1den.facetertest.presentation.screens.onboarding.presenter.OnboardingPresenter
import com.rv1den.facetertest.presentation.screens.onboarding.view.OnboardingActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ProjectComponent::class], modules = [OnboardingModule::class])
interface OnboardingComponent {
    fun inject(onboardingActivity: OnboardingActivity)
    fun inject(onboardingPresenter: OnboardingPresenter)
}