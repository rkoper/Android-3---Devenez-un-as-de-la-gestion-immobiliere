package com.sofianem.realestatemanager.viewmodel

import androidx.lifecycle.*
import com.sofianem.realestatemanager.data.model.*
import com.sofianem.realestatemanager.data.repository.PlaceRepo
import com.sofianem.realestatemanager.services.MapService
import com.sofianem.realestatemanager.utils.Utils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt

class MyViewModelForPlaces(private val mRepositoryPlace : PlaceRepo) : ViewModel() {

    fun saveL1(location: String, type: String, id: Int) {
        mRepositoryPlace.saveL1(location, type, id) }

    fun getByIdLocation(type: String, master_id: Int): LiveData<List<NearbyPlaces>> {
        return mRepositoryPlace.getByIdLocation(type, master_id)
    }

}