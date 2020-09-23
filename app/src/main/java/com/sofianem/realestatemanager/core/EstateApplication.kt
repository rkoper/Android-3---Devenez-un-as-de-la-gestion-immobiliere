package com.sofianem.realestatemanager.core

import android.app.Application
import com.sofianem.realestatemanager.di.*
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