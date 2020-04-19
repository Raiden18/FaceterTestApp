package com.rv1den.facetertest.presentation.app

import android.app.Application
import android.hardware.camera2.CameraManager
import com.livetyping.permission.PermissionBinder
import com.rv1den.facetertest.domain.reposiories.CameraRepository
import com.rv1den.facetertest.domain.usecases.GetCameraUseCase
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [GlobalModule::class])
interface ProjectComponent {

    fun providePermissionBinder(): PermissionBinder
    fun provideApplication(): Application
    fun provideGetCameraUseCase(): GetCameraUseCase
    fun provideCameraRepository(): CameraRepository
    fun provideCameraManager(): CameraManager

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun provideApplication(application: Application): Builder

        fun build(): ProjectComponent
    }

}