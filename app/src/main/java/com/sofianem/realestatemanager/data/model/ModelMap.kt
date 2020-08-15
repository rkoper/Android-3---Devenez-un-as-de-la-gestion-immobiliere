package com.sofianem.realestatemanager.data.model

import com.google.gson.annotations.SerializedName


/**
 * Created by david on 15/02/2019.
 */
data class Location(@SerializedName("lat") val lat: Double, @SerializedName("lng") val lng: Double)

data class Geometry(@SerializedName("location") val location: Location)

lateinit var placesList: Place

data class Place(
    @SerializedName("name") val name: String,
    @SerializedName("geometry") val geometry: Geometry,
    @SerializedName("types") val types: ArrayList<String>
)

data class PlacesResponse1(
    @SerializedName("results") val placesList1: ArrayList<Place>?
)

data class PlacesResponse2(
    @SerializedName("results") val placesList2: ArrayList<Place>?
)

data class PlacesResponse3(
    @SerializedName("results") val placesList3: ArrayList<Place>?
)

data class PlacesResponse4(
    @SerializedName("results") val placesList4: ArrayList<Place>?
)
