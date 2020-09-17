package com.sofianem.realestatemanager

import android.app.Application
import androidx.room.Room
import com.sofianem.realestatemanager.data.dao.EstateDao
import com.sofianem.realestatemanager.data.dao.ImageDao
import com.sofianem.realestatemanager.data.dao.PlaceDao
import com.sofianem.realestatemanager.data.dataBase.AllDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val DataBaseModule = module {

    fun provideDatabase(application: Application): AllDatabase {
        return Room.databaseBuilder(application, AllDatabase::class.java, "RealEstateManager.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideEstateDao(database: AllDatabase): EstateDao { return  database.estateDao() }
    fun provideImageDao(database: AllDatabase): ImageDao { return  database.imageDao() }
    fun providePlaceDao(database: AllDatabase): PlaceDao { return  database.nearbyPlaceDao() }

    single { provideDatabase(androidApplication()) }
    single { provideEstateDao(get()) }
    single { provideImageDao(get()) }
    single { providePlaceDao(get()) }


}