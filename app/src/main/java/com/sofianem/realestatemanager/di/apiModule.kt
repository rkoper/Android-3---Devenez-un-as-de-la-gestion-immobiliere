package com.sofianem.realestatemanager.di

import com.sofianem.realestatemanager.services.MapService
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {

    fun provideApi(retrofit: Retrofit): MapService {
        return retrofit.create(MapService::class.java)
    }
    single { provideApi(get()) }

}