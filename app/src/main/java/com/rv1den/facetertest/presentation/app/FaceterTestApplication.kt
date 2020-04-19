package com.rv1den.facetertest.presentation.app

import android.app.Application

class FaceterTestApplication : Application() {
    lateinit var projectComponent: ProjectComponent

    override fun onCreate() {
        super.onCreate()
        projectComponent = DaggerProjectComponent.builder()
            .provideApplication(this)
            .build()
    }
}