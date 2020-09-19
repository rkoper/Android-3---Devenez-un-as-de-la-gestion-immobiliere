package com.sofianem.realestatemanager

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class EstateApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@EstateApplication)
            modules(
                apiModule,
                viewModelModule,
                repositoryModule,
                networkModule,
                DataBaseModule
            )
        }
    }
}