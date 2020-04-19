package com.rv1den.facetertest.presentation.features.camera.di

import com.rv1den.facetertest.presentation.app.ProjectComponent
import com.rv1den.facetertest.presentation.features.camera.view.CameraActivity
import com.rv1den.facetertest.presentation.features.di.ActivityScope
import dagger.Component

@ActivityScope
@Component(dependencies = [ProjectComponent::class], modules = [CameraModule::class])
interface CameraComponent {
    fun inject(cameraActivity: CameraActivity)
}
