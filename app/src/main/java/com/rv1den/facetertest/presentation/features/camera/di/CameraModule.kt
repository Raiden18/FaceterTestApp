package com.rv1den.facetertest.presentation.features.camera.di

import android.app.Application
import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import com.rv1den.facetertest.domain.usecases.GetCameraUseCase
import com.rv1den.facetertest.presentation.features.camera.presenter.CameraPresenterProvider
import com.rv1den.facetertest.presentation.features.di.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class CameraModule {

    @Provides
    @ActivityScope
    fun providePresenterProvider(
        getCameraUseCase: GetCameraUseCase
    ): CameraPresenterProvider {
        return CameraPresenterProvider(getCameraUseCase)
    }
}