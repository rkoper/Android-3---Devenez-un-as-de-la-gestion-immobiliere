package com.sofianem.realestatemanager.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.sofianem.realestatemanager.data.dao.PlaceDao
import com.sofianem.realestatemanager.data.dataBase.AllDatabase
import com.sofianem.realestatemanager.data.model.NearbyPlaces
import kotlinx.coroutines.*

open class PlaceRepo (application: Application) {

    private val nearbyPlaceDao: PlaceDao
    var readAllPlace: LiveData<List<NearbyPlaces>>

    init {
        val database = AllDatabase.getInstance(application.applicationContext)
        nearbyPlaceDao = database!!.nearbyPlaceDao()
        readAllPlace = nearbyPlaceDao.getPlaceAll()
    }

    fun insertLoc1(nearbyPlaces: NearbyPlaces) = runBlocking {
        this.launch(Dispatchers.IO) {
            nearbyPlaceDao.insertLoc1(nearbyPlaces) }
    }

    fun insertLoc2(nearbyPlaces: NearbyPlaces) = runBlocking {
        this.launch(Dispatchers.IO) {
            nearbyPlaceDao.insertLoc2(nearbyPlaces) }
    }

    fun insertLoc3(nearbyPlaces: NearbyPlaces) = runBlocking {
        this.launch(Dispatchers.IO) {
            nearbyPlaceDao.insertLoc3(nearbyPlaces) }
    }

    fun insertLoc4(nearbyPlaces: NearbyPlaces) = runBlocking {
        this.launch(Dispatchers.IO) {
            nearbyPlaceDao.insertLoc4(nearbyPlaces) }
    }

    fun getByIdLocation1 (type: String, master_id: Int) : LiveData<List<NearbyPlaces>>
            = nearbyPlaceDao.getByIdLocation1(type, master_id)


}

