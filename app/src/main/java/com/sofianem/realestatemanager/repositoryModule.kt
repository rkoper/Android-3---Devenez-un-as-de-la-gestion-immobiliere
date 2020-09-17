package com.sofianem.realestatemanager

import android.content.Context
import com.sofianem.realestatemanager.data.dao.EstateDao
import com.sofianem.realestatemanager.data.dao.ImageDao
import com.sofianem.realestatemanager.data.dao.PlaceDao
import com.sofianem.realestatemanager.data.repository.EstateRepo
import com.sofianem.realestatemanager.data.repository.ImageRepo
import com.sofianem.realestatemanager.data.repository.PlaceRepo
import com.sofianem.realestatemanager.services.MapService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    fun provideEstateRepository(mEstateDao : EstateDao): EstateRepo {
        return EstateRepo (mEstateDao) }
    fun provideImageRepository(mImageDao : ImageDao): ImageRepo {
        return ImageRepo(mImageDao) }
    fun providePlaceRepository(mPlaceDao : PlaceDao): PlaceRepo {
        return PlaceRepo( mPlaceDao) }


    single { provideEstateRepository( get()) }
    single { provideImageRepository(get()) }
    single { providePlaceRepository(get()) }

}