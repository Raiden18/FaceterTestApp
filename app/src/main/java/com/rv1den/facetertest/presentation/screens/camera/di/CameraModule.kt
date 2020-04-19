package com.rv1den.facetertest.presentation.screens.camera.di

import android.app.Application
import androidx.camera.lifecycle.ProcessCameraProvider
import com.google.common.util.concurrent.ListenableFuture
import com.rv1den.facetertest.presentation.screens.camera.presenter.CameraPresenterProvider
import com.rv1den.facetertest.presentation.screens.camera.view.factories.camerax.BackCameraFactory
import com.rv1den.facetertest.presentation.screens.camera.view.factories.camerax.ImageCaptureFactory
import com.rv1den.facetertest.presentation.screens.camera.view.factories.camerax.PreviewFactory
import com.rv1den.facetertest.presentation.screens.camera.view.factories.file.ImageFileFactory
import com.rv1den.facetertest.presentation.screens.camera.view.factories.file.OutputDirectoryFactory
import com.rv1den.facetertest.presentation.screens.di.ActivityScope
import dagger.Module
import dagger.Provides
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Module
class CameraModule {
    @Provides
    @ActivityScope
    fun provideImageCaptureFactory() = ImageCaptureFactory()

    @Provides
    fun provideCameraPresenterProvider() = CameraPresenterProvider()

    @Provides
    @ActivityScope
    fun providePreviewFactory() = PreviewFactory()

    @Provides
    @ActivityScope
    fun provideImageFileFactory(): ImageFileFactory {
        val outputDirectoryFactory = OutputDirectoryFactory()
        return ImageFileFactory(outputDirectoryFactory)
    }

    @Provides
    @ActivityScope
    fun provideBackCameraFactory() = BackCameraFactory()

    @Provides
    @ActivityScope
    fun provideCameraExecutor() = Executors.newSingleThreadExecutor()

    @Provides
    @ActivityScope
    fun provideProcessCameraProvider(application: Application): ListenableFuture<ProcessCameraProvider> {
        return ProcessCameraProvider.getInstance(application)
    }
}