package com.rv1den.facetertest.presentation.features.onboarding.di

import com.rv1den.facetertest.presentation.app.ProjectComponent
import com.rv1den.facetertest.presentation.features.di.ActivityScope
import com.rv1den.facetertest.presentation.features.onboarding.presenter.OnboardingPresenter
import com.rv1den.facetertest.presentation.features.onboarding.view.OnboardingActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ProjectComponent::class], modules = [OnboardingModule::class])
interface OnboardingComponent {
    fun inject(onboardingActivity: OnboardingActivity)
    fun inject(onboardingPresenter: OnboardingPresenter)
}