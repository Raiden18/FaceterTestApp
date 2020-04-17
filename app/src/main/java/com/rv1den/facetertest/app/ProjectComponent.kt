package com.rv1den.facetertest.app

import android.app.Application
import com.livetyping.permission.PermissionBinder
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [GlobalModule::class])
interface ProjectComponent {

    fun providePermissionBinder(): PermissionBinder
    fun provideApplication(): Application

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun provideApplication(application: Application): Builder

        fun build(): ProjectComponent
    }

}