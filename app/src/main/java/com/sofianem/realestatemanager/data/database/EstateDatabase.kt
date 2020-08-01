package com.sofianem.realestatemanager.data.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sofianem.realestatemanager.data.Dao.EstateDao
import com.sofianem.realestatemanager.data.Model.EstateR

@Database(entities = [EstateR::class], version = 1)
abstract class EstateDatabase : RoomDatabase() {

    abstract fun estateDao(): EstateDao


    companion object {

        @Volatile
        private var INSTANCE: EstateDatabase? = null

        fun getInstance(context: Context): EstateDatabase? {
            if (INSTANCE == null) { synchronized(EstateDatabase::class.java) {
                if (INSTANCE == null) { INSTANCE = Room.databaseBuilder(context.applicationContext, EstateDatabase::class.java, "MyDatabase.db")
                            .build() } } }
            return INSTANCE } } }
