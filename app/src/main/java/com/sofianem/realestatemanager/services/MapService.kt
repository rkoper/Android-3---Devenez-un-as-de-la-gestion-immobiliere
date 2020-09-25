package com.sofianem.realestatemanager.services

import com.sofianem.realestatemanager.data.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET


/**
 * Created by david on 15/02/2019.
 */
interface MapService {
    var mResponse: Response<PlacesResponse1>

    @GET("nearbysearch/json?key=AIzaSyByK0jz-yxjpZFX88W8zjzTwtzMtkPYC4w")
    fun getNearbyPlaces1(
        @retrofit2.http.Query("location") location: String,
        @retrofit2.http.Query("type") type: String,
        @retrofit2.http.Query("rankby") rankBy: String = "distance"
    ) : Call<PlacesResponse1>


    @GET("nearbysearch/json?key=AIzaSyByK0jz-yxjpZFX88W8zjzTwtzMtkPYC4w")
    fun getNearbyPlaces2(
        @retrofit2.http.Query("location") location: String,
        @retrofit2.http.Query("type") type: String,
        @retrofit2.http.Query("rankby") rankBy: String = "distance"
    ): Call<PlacesResponse2>

    @GET("nearbysearch/json?key=AIzaSyByK0jz-yxjpZFX88W8zjzTwtzMtkPYC4w")
    fun getNearbyPlaces3(
        @retrofit2.http.Query("location") location: String,
        @retrofit2.http.Query("type") type: String,
        @retrofit2.http.Query("rankby") rankBy: String = "distance"
    ): Call<PlacesResponse3>

    @GET("nearbysearch/json?key=AIzaSyByK0jz-yxjpZFX88W8zjzTwtzMtkPYC4w")
    fun getNearbyPlaces4(
        @retrofit2.http.Query("location") location: String,
        @retrofit2.http.Query("type") type: String,
        @retrofit2.http.Query("rankby") rankBy: String = "distance"
    ): Call<PlacesResponse4>


   companion object

}