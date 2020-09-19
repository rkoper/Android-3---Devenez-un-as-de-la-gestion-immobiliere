package com.sofianem.realestatemanager.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.sofianem.realestatemanager.data.dao.PlaceDao
import com.sofianem.realestatemanager.data.model.NearbyPlaces
import com.sofianem.realestatemanager.data.model.PlacesResponse1
import com.sofianem.realestatemanager.services.MapService
import com.sofianem.realestatemanager.utils.Utils
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class PlaceRepo (mService : MapService, mContext: Context,  mPlaceDao : PlaceDao) {
    val mService = mService
    val mContext = mContext
    private val mPlaceDao = mPlaceDao
    var np = NearbyPlaces()


    fun getByIdLocation(type: String, master_id: Int): LiveData<List<NearbyPlaces>> =
        mPlaceDao.getByIdLocation(type, master_id)

    fun saveL1(location: String, type: String, id: Int) {
        var mResp: Response<PlacesResponse1>
        var call = mService.getNearbyPlaces1(location, type)

        call.enqueue(object : Callback<PlacesResponse1> {
            override fun onFailure(call: Call<PlacesResponse1>, t: Throwable) {}
            override fun onResponse(call: Call<PlacesResponse1>, response1: Response<PlacesResponse1>) {
                mResp = response1
                mResp.body()?.let {
                        placeResponse ->
                    val placeResponseArray = placeResponse.placesList1
                    placeResponseArray?.forEach {
                            place ->

                        val curLat = Utils.currentLat(location)
                        val curLng = Utils.currentLng(location)
                        val distance = Utils.calculateDistance(curLat, curLng, place.geometry.location.lat, place.geometry.location.lng).roundToInt()

                        if (place.types[0] == type || place.types[1] == type) { np.placetype = type }
                        else { np.placetype = place.types[0] }

                        np.placeLocation = (place.geometry.location.lat.toString() + "," + place.geometry.location.lng.toString())
                        np.placeMasterId = id
                        np.placeName = place.name
                        np.placeDistance = distance

                        println("placeName------>> " + place.name)
                        mPlaceDao.insertLoc1(np)

                    }
                }

            }
    })




    }
}