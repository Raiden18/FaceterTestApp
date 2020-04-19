package com.rv1den.facetertest.presentation.app

import android.app.Application
import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import com.livetyping.permission.PermissionBinder
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
    fun providePermissionBinder(): PermissionBinder = PermissionBinder()
}