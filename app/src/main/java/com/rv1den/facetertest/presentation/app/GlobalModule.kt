package com.rv1den.facetertest.presentation.app

import android.app.Application
import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import com.livetyping.permission.PermissionBinder
import com.rv1den.facetertest.data.repositories.CameraRepositoryImpl
import com.rv1den.facetertest.domain.reposiories.CameraRepository
import com.rv1den.facetertest.domain.usecases.GetCameraUseCase
import com.rv1den.facetertest.presentation.features.di.ActivityScope
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GlobalModule {

    @Singleton
    @Provides
    fun provideCameraManager(application: Application): CameraManager {
        return application.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    @Singleton
    @Provides
    fun provideCameraCharacteristic(cameraManager: CameraManager): CameraCharacteristics {
        return cameraManager.getCameraCharacteristics("asdasd")
    }

    @Singleton
    @Provides
    fun providePermissionBinder(): PermissionBinder = PermissionBinder()

    @Singleton
    @Provides
    fun provideCameraRepository(cameraManager: CameraManager): CameraRepository {
        return CameraRepositoryImpl(cameraManager)
    }

    @Singleton
    @Provides
    fun provideGetCameraUseCase(cameraRepository: CameraRepository): GetCameraUseCase {
        return GetCameraUseCase(cameraRepository)
    }
}