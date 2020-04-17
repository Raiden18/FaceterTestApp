package com.rv1den.facetertest.features.onboarding.di

import com.rv1den.facetertest.app.ProjectComponent
import com.rv1den.facetertest.features.di.ActivityScope
import com.rv1den.facetertest.features.onboarding.presenter.OnboardingPresenter
import com.rv1den.facetertest.features.onboarding.view.OnboardingActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ProjectComponent::class], modules = [OnboardingModule::class])
interface OnboardingComponent {
    fun inject(onboardingActivity: OnboardingActivity)
    fun inject(onboardingPresenter: OnboardingPresenter)
}