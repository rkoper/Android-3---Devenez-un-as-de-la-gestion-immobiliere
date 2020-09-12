package com.sofianem.realestatemanager.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.sofianem.realestatemanager.data.dao.PlaceDao
import com.sofianem.realestatemanager.data.dataBase.ImageDatabase
import com.sofianem.realestatemanager.data.model.ImageV
import com.sofianem.realestatemanager.data.model.NearbyPlaces
import kotlinx.coroutines.*

open class PlaceRepo (application: Application) {

    private val nearbyPlaceDao: PlaceDao
    var readAllPlace: LiveData<List<NearbyPlaces>>

    init {
        val database = ImageDatabase.getInstance(application.applicationContext)
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



    fun getByIdLocation2 (type: String, master_id: Int) : LiveData<List<NearbyPlaces>>
            = nearbyPlaceDao.getByIdLocation2(type, master_id)

    fun getByIdLocation1 (type: String, master_id: Int) : LiveData<List<NearbyPlaces>>
            = nearbyPlaceDao.getByIdLocation1(type, master_id)

    fun getByIdLocation3 (type: String, master_id: Int) : LiveData<List<NearbyPlaces>>
        = nearbyPlaceDao.getByIdLocation3(type, master_id)

    fun getByIdLocation4 (type: String, master_id: Int) : LiveData<List<NearbyPlaces>>
        = nearbyPlaceDao.getByIdLocation4(type, master_id)

    fun getByIdLocation11 (type: String, master_id: Int) : MutableList<NearbyPlaces> = runBlocking {
        var e: MutableList<NearbyPlaces> = arrayListOf()
        GlobalScope.launch(Dispatchers.IO) { e = nearbyPlaceDao.getByIdLocation(type, master_id) }
        return@runBlocking e }

    fun getByIdLocation21 (type: String, master_id: Int) : MutableList<NearbyPlaces> = runBlocking {
        var f: MutableList<NearbyPlaces> = arrayListOf()
        GlobalScope.launch(Dispatchers.IO) { f = nearbyPlaceDao.getByIdLocation(type, master_id) }
        return@runBlocking f }

    fun getByIdLocation31 (type: String, master_id: Int) : MutableList<NearbyPlaces> = runBlocking {
        var g: MutableList<NearbyPlaces> = arrayListOf()
        GlobalScope.launch(Dispatchers.IO) { g = nearbyPlaceDao.getByIdLocation(type, master_id) }
        return@runBlocking g }

    fun getByIdLocation41 (type: String, master_id: Int) : MutableList<NearbyPlaces> = runBlocking {
        var h: MutableList<NearbyPlaces> = arrayListOf()
        GlobalScope.launch(Dispatchers.IO) { h = nearbyPlaceDao.getByIdLocation(type, master_id) }
        return@runBlocking h }



}

