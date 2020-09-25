package com.sofianem.realestatemanager.services

import com.sofianem.realestatemanager.data.model.PlacesResponse1
import retrofit2.Call
import retrofit2.http.GET


/**
 * Created by david on 15/02/2019.
 */
interface MapService {

    @GET("nearbysearch/json?key=AIzaSyByK0jz-yxjpZFX88W8zjzTwtzMtkPYC4w")
    fun getNearbyPlaces1(
        @retrofit2.http.Query("location") location: String,
        @retrofit2.http.Query("type") type: String,
        @retrofit2.http.Query("rankby") rankBy: String = "distance"
    ) : Call<PlacesResponse1>


    companion object

}