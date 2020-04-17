package com.rv1den.facetertest.features.camera.di

import com.rv1den.facetertest.app.ProjectComponent
import com.rv1den.facetertest.features.camera.view.CameraActivity
import com.rv1den.facetertest.features.di.ActivityScope
import dagger.Component

@ActivityScope
@Component(dependencies = [ProjectComponent::class], modules = [CameraModule::class])
interface CameraComponent {
    fun inject(cameraActivity: CameraActivity)
}
