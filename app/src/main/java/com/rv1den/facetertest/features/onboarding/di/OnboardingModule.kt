package com.rv1den.facetertest.features.onboarding.di

import android.app.Application
import com.livetyping.permission.PermissionBinder
import com.rv1den.facetertest.features.di.ActivityScope
import com.rv1den.facetertest.features.onboarding.presenter.OnboardingNavigator
import com.rv1den.facetertest.features.onboarding.view.OnboardingPresenterProvider
import com.rv1den.facetertest.features.onboarding.presenter.PermissionRequester
import com.rv1den.facetertest.features.onboarding.view.OnboardingNavigatorImpl
import com.rv1den.facetertest.features.onboarding.view.PermissionRequesterImpl
import dagger.Module
import dagger.Provides

@Module
class OnboardingModule {

    @Provides
    @ActivityScope
    fun providePresenterProvider(
        permissionRequester: PermissionRequester,
        onboardingNavigator: OnboardingNavigator
    ): OnboardingPresenterProvider {
        return OnboardingPresenterProvider(
            permissionRequester,
            onboardingNavigator
        )
    }

    @Provides
    @ActivityScope
    fun provideOnboardingNavigator(application: Application): OnboardingNavigator {
        return OnboardingNavigatorImpl(application)
    }

    @Provides
    @ActivityScope
    fun providePermissionRequester(
        permissionBinder: PermissionBinder,
        application: Application
    ): PermissionRequester {
        return PermissionRequesterImpl(permissionBinder, application)
    }
}