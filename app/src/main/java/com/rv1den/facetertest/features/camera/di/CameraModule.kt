package com.rv1den.facetertest.features.camera.di

import com.rv1den.facetertest.features.camera.presenter.CameraPresenterProvider
import com.rv1den.facetertest.features.di.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class CameraModule {

    @Provides
    @ActivityScope
    fun providePresenterProvider(): CameraPresenterProvider {
        return CameraPresenterProvider()
    }
}