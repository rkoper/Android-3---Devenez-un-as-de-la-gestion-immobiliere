package com.sofianem.realestatemanager.utils

import android.app.Application
import android.content.Context

class MyApplication : Application() {

        init {
            instance = this
        }

        companion object {
            private var instance: MyApplication? = null

            fun applicationContext() : Context {
                return instance!!.applicationContext
            }
        }

        override fun onCreate() {
            super.onCreate()
            // initialize for any

            // Use ApplicationContext.
            // example: SharedPreferences etc...
            val context: Context = MyApplication.applicationContext()
        }
    }