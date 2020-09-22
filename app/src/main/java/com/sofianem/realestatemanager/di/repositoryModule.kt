package com.sofianem.realestatemanager.di

import android.content.Context
import com.sofianem.realestatemanager.data.dao.EstateDao
import com.sofianem.realestatemanager.data.dao.ImageDao
import com.sofianem.realestatemanager.data.dao.PlaceDao
import com.sofianem.realestatemanager.repository.EstateRepo
import com.sofianem.realestatemanager.repository.ImageRepo
import com.sofianem.realestatemanager.repository.PlaceRepo
import com.sofianem.realestatemanager.services.MapService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    fun provideEstateRepository(mEstateDao : EstateDao): EstateRepo {
        return EstateRepo (mEstateDao) }
    fun provideImageRepository(mImageDao : ImageDao): ImageRepo {
        return ImageRepo(mImageDao) }
    fun providePlaceRepository(mService : MapService, mContext: Context,  mPlaceDao : PlaceDao): PlaceRepo {
        return PlaceRepo( mService, mContext, mPlaceDao) }


    single { provideEstateRepository( get()) }
    single { provideImageRepository(get()) }
    single { providePlaceRepository(get(), androidContext(), get()) }

}