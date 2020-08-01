package com.sofianem.realestatemanager.services

import com.sofianem.realestatemanager.data.Model.PlacesResponse1
import com.sofianem.realestatemanager.data.Model.PlacesResponse2
import com.sofianem.realestatemanager.data.Model.PlacesResponse3
import com.sofianem.realestatemanager.data.Model.PlacesResponse4
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
    ): retrofit2.Call<PlacesResponse1>

    @GET("nearbysearch/json?key=AIzaSyByK0jz-yxjpZFX88W8zjzTwtzMtkPYC4w")
    fun getNearbyPlaces2(
        @retrofit2.http.Query("location") location: String,
        @retrofit2.http.Query("type") type: String,
        @retrofit2.http.Query("rankby") rankBy: String = "distance"
    ): retrofit2.Call<PlacesResponse2>

    @GET("nearbysearch/json?key=AIzaSyByK0jz-yxjpZFX88W8zjzTwtzMtkPYC4w")
    fun getNearbyPlaces3(
        @retrofit2.http.Query("location") location: String,
        @retrofit2.http.Query("type") type: String,
        @retrofit2.http.Query("rankby") rankBy: String = "distance"
    ): retrofit2.Call<PlacesResponse3>

    @GET("nearbysearch/json?key=AIzaSyByK0jz-yxjpZFX88W8zjzTwtzMtkPYC4w")
    fun getNearbyPlaces4(
        @retrofit2.http.Query("location") location: String,
        @retrofit2.http.Query("type") type: String,
        @retrofit2.http.Query("rankby") rankBy: String = "distance"
    ): retrofit2.Call<PlacesResponse4>



}