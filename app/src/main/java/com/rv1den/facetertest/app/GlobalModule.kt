package com.rv1den.facetertest.app

import com.livetyping.permission.PermissionBinder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GlobalModule {

    @Singleton
    @Provides
    fun providePermissionBinder(): PermissionBinder = PermissionBinder()
}