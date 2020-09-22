package com.sofianem.realestatemanager.viewmodel

import androidx.lifecycle.*
import com.sofianem.realestatemanager.data.model.*
import com.sofianem.realestatemanager.repository.PlaceRepo

class MyViewModelForPlaces(private val mRepositoryPlace : PlaceRepo) : ViewModel() {

    fun saveLocation(location: String, type: String, id: Int) {
        mRepositoryPlace.saveLocation(location, type, id) }

    fun getByIdLocation(type: String, master_id: Int): LiveData<List<NearbyPlaces>> {
        return mRepositoryPlace.getByIdLocation(type, master_id)
    }

}