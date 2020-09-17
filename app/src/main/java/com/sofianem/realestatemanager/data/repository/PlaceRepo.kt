package com.sofianem.realestatemanager.data.repository

import androidx.lifecycle.LiveData
import com.sofianem.realestatemanager.data.dao.PlaceDao
import com.sofianem.realestatemanager.data.model.NearbyPlaces
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

open class PlaceRepo (nearbyPlaceDao: PlaceDao) {

    private val nearbyPlaceDao = nearbyPlaceDao
    var readAllPlace: LiveData<List<NearbyPlaces>> = nearbyPlaceDao.getPlaceAll()

    fun insertLoc1(nearbyPlaces: NearbyPlaces) { GlobalScope.launch(Dispatchers.IO) { nearbyPlaceDao.insertLoc1(nearbyPlaces) } }
    fun insertLoc2(nearbyPlaces: NearbyPlaces)  { GlobalScope.launch(Dispatchers.IO) { nearbyPlaceDao.insertLoc2(nearbyPlaces) } }
    fun insertLoc3(nearbyPlaces: NearbyPlaces) { GlobalScope.launch(Dispatchers.IO) { nearbyPlaceDao.insertLoc3(nearbyPlaces) } }
    fun insertLoc4(nearbyPlaces: NearbyPlaces) { GlobalScope.launch(Dispatchers.IO) { nearbyPlaceDao.insertLoc4(nearbyPlaces) } }

    fun getByIdLocation(type: String, master_id: Int) : LiveData<List<NearbyPlaces>>
            = nearbyPlaceDao.getByIdLocation(type, master_id)

}