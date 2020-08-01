package com.sofianem.realestatemanager.data.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sofianem.realestatemanager.data.Model.NearbyPlaces
import com.sofianem.realestatemanager.data.Dao.PlaceDao
import com.sofianem.realestatemanager.data.Dao.EstateDao
import com.sofianem.realestatemanager.data.Model.EstateR
import com.sofianem.realestatemanager.data.Dao.ImageDao
import com.sofianem.realestatemanager.data.Model.ImageV

@Database(entities = (arrayOf(ImageV::class, EstateR::class, NearbyPlaces::class)), version = 1, exportSchema = false)
abstract class ImageDatabase : RoomDatabase() {

    abstract fun imageDao(): ImageDao
    abstract fun estateDao(): EstateDao
    abstract fun nearbyPlaceDao() : PlaceDao

    companion object {
        private var INSTANCE: ImageDatabase? = null

        fun getInstance(context: Context): ImageDatabase {
            if (INSTANCE == null) { synchronized(this) { INSTANCE = Room.databaseBuilder(
                        context.applicationContext, ImageDatabase::class.java, "RealEstateManager.db").build() } }
            return INSTANCE as ImageDatabase
        } } }
