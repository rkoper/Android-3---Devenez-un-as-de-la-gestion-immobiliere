package com.sofianem.realestatemanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sofianem.realestatemanager.data.dataBase.AllDatabase
import com.sofianem.realestatemanager.data.model.*
import com.sofianem.realestatemanager.data.repository.ImageRepo
import com.sofianem.realestatemanager.data.repository.PlaceRepo
import com.sofianem.realestatemanager.services.MapService
import com.sofianem.realestatemanager.utils.Utils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt

class MyViewModelForPlaces(application: Application) : AndroidViewModel(application) {
    private val mImageDb: AllDatabase? = com.sofianem.realestatemanager.data.dataBase.AllDatabase.getInstance(application)
    private val mResponseData1: ArrayList<PlacesResponse1?> = arrayListOf()
    private val mResponseData2: ArrayList<PlacesResponse2?> = arrayListOf()
    private val mResponseData3: ArrayList<PlacesResponse3?> = arrayListOf()
    private val mResponseData4: ArrayList<PlacesResponse4?> = arrayListOf()
    private val mAllDataPark = MutableLiveData<List<NearbyPlaces>>()
    private val mAllDataMarket = MutableLiveData<List<NearbyPlaces>>()
    private val mAllDataSchool = MutableLiveData<List<NearbyPlaces>>()
    private val mAllDataPharmacy = MutableLiveData<List<NearbyPlaces>>()
    private val mAllDataForLoc1 = MutableLiveData<List<NearbyPlaces>>()
    private val mAllDataForLoc2 = MutableLiveData<List<NearbyPlaces>>()
    private val mAllDataForLoc3 = MutableLiveData<List<NearbyPlaces>>()
    private val mAllDataForLoc4 = MutableLiveData<List<NearbyPlaces>>()

    private val mRepositoryPlace: PlaceRepo = PlaceRepo(application)
    val allPlace: LiveData<List<NearbyPlaces>>

    init { allPlace = mRepositoryPlace.readAllPlace }


    fun getNearbyPlace1(id: Int, location: String) {
        var interceptor = HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build();
        var retrofit: Retrofit = Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/api/place/").addConverterFactory(
            GsonConverterFactory.create()).client(client).build();
        val service = retrofit.create(MapService::class.java)
        var call = service.getNearbyPlaces1(location, "pharmacy")
        call.enqueue(object :
            Callback<PlacesResponse1> { override fun onFailure(call: Call<PlacesResponse1>, t: Throwable) {}
            override fun onResponse(call: Call<PlacesResponse1>, response1: Response<PlacesResponse1>) {
                mResponseData1.add(response1.body());   nearbySaving1(mResponseData1, location, id) } }) }

    private fun nearbySaving1(response: ArrayList<PlacesResponse1?>, locationForPlace: String, id: Int) {
        response.forEach { allPlace ->
            allPlace?.placesList1?.forEach { place -> val np = NearbyPlaces()
                val curLat = Utils.currentLat(locationForPlace) ; val curLng = Utils.currentLng(locationForPlace)
                val distance = Utils.calculateDistance(curLat, curLng, place.geometry.location.lat, place.geometry.location.lng).roundToInt()
                if (place.types[0] == "park" || place.types[1] == "park")
                { np.placetype = "park" }
                else { np.placetype = place.types[0] }
                np.placeLocation = (place.geometry.location.lat.toString() + "," + place.geometry.location.lng.toString())
                np.placeMasterId = id
                np.placeName = place.name
                np.placeDistance = distance
                saveNearby1(np) } } }

    fun getNearbyPlace2(id: Int, location: String) {
        var interceptor = HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build();
        var retrofit: Retrofit = Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/api/place/").addConverterFactory(
            GsonConverterFactory.create()).client(client).build();
        val service = retrofit.create(MapService::class.java)
        var call = service.getNearbyPlaces2(location, "park")
        call.enqueue(object :
            Callback<PlacesResponse2> { override fun onFailure(call: Call<PlacesResponse2>, t: Throwable) {}
            override fun onResponse(call: Call<PlacesResponse2>, response2: Response<PlacesResponse2>) {
                mResponseData2.add(response2.body());   nearbySaving2(mResponseData2, location, id) } }) }

    private fun nearbySaving2(response: ArrayList<PlacesResponse2?>, locationForPlace: String, id: Int) {
        response.forEach { allPlace ->
            allPlace?.placesList2?.forEach { place -> val np = NearbyPlaces()
                val curLat = Utils.currentLat(locationForPlace) ; val curLng = Utils.currentLng(locationForPlace)
                val distance = Utils.calculateDistance(curLat, curLng, place.geometry.location.lat, place.geometry.location.lng).roundToInt()
                if (place.types[0] == "park" || place.types[1] == "park") { np.placetype = "park" } else { np.placetype = place.types[0] }
                np.placeLocation = (place.geometry.location.lat.toString() + "," + place.geometry.location.lng.toString())
                np.placeMasterId = id; np.placeName = place.name; np.placeDistance = distance
                saveNearby2(np) } } }

    fun getNearbyPlace3(id: Int, location: String) {
        var interceptor = HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build();
        var retrofit: Retrofit = Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/api/place/").addConverterFactory(
            GsonConverterFactory.create()).client(client).build();
        val service = retrofit.create(MapService::class.java)
        var call = service.getNearbyPlaces3(location, "primary_school")
        call.enqueue(object :
            Callback<PlacesResponse3> { override fun onFailure(call: Call<PlacesResponse3>, t: Throwable) {}
            override fun onResponse(call: Call<PlacesResponse3>, response3: Response<PlacesResponse3>) {
                mResponseData3.add(response3.body());   nearbySaving3(mResponseData3, location, id) } }) }

    private fun nearbySaving3(response: ArrayList<PlacesResponse3?>, locationForPlace: String, id: Int) {
        response.forEach { allPlace ->
            allPlace?.placesList3?.forEach { place -> val np = NearbyPlaces()
                val curLat = Utils.currentLat(locationForPlace) ; val curLng = Utils.currentLng(locationForPlace)
                val distance = Utils.calculateDistance(curLat, curLng, place.geometry.location.lat, place.geometry.location.lng).roundToInt()
                if (place.types[0] == "park" || place.types[1] == "park") { np.placetype = "park" } else { np.placetype = place.types[0] }
                np.placeLocation = (place.geometry.location.lat.toString() + "," + place.geometry.location.lng.toString())
                np.placeMasterId = id; np.placeName = place.name; np.placeDistance = distance
                saveNearby3(np) } } }

    fun getNearbyPlace4(id: Int, location: String) {
        var interceptor = HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build();
        var retrofit: Retrofit = Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/api/place/").addConverterFactory(
            GsonConverterFactory.create()).client(client).build();
        val service = retrofit.create(MapService::class.java)
        var call = service.getNearbyPlaces4(location, "supermarket")
        call.enqueue(object :
            Callback<PlacesResponse4> { override fun onFailure(call: Call<PlacesResponse4>, t: Throwable) {}
            override fun onResponse(call: Call<PlacesResponse4>, response4: Response<PlacesResponse4>) {
                mResponseData4.add(response4.body());   nearbySaving4(mResponseData4, location, id) } }) }

    private fun nearbySaving4(response: ArrayList<PlacesResponse4?>, locationForPlace: String, id: Int) {
        response.forEach { allPlace ->
            allPlace?.placesList4?.forEach { place -> val np = NearbyPlaces()
                val curLat = Utils.currentLat(locationForPlace) ; val curLng = Utils.currentLng(locationForPlace)
                val distance = Utils.calculateDistance(curLat, curLng, place.geometry.location.lat, place.geometry.location.lng).roundToInt()
                if (place.types[0] == "park" || place.types[1] == "park") { np.placetype = "park" } else { np.placetype = place.types[0] }
                np.placeLocation = (place.geometry.location.lat.toString() + "," + place.geometry.location.lng.toString())
                np.placeMasterId = id; np.placeName = place.name; np.placeDistance = distance
                saveNearby4(np) } } }


    fun saveNearby1(np: NearbyPlaces) {
        mRepositoryPlace.insertLoc1(np) }

    fun saveNearby2(np: NearbyPlaces){ mRepositoryPlace.insertLoc2(np) }

    fun saveNearby3(np: NearbyPlaces) { mRepositoryPlace.insertLoc3(np) }

    fun saveNearby4(np: NearbyPlaces) { mRepositoryPlace.insertLoc4(np) }

    fun getByIdLocation(type: String, master_id: Int): LiveData<List<NearbyPlaces>> {
        val a = mRepositoryPlace.getByIdLocation1(type, master_id)
        return a }

    fun getByIdLocation1(type: String, master_id: Int): LiveData<List<NearbyPlaces>> {
        val a = mRepositoryPlace.getByIdLocation11(type, master_id)
        mAllDataSchool.postValue(a)
        return mAllDataForLoc1 }

    fun getByIdLocation2(type: String, master_id: Int): LiveData<List<NearbyPlaces>> {
        val a = mRepositoryPlace.getByIdLocation21(type, master_id)
        mAllDataSchool.postValue(a)
        return mAllDataForLoc2 }

    fun getByIdLocation3(type: String, master_id: Int): LiveData<List<NearbyPlaces>> {
        val a = mRepositoryPlace.getByIdLocation31(type, master_id)
        mAllDataSchool.postValue(a)
        return mAllDataForLoc3 }

    fun getByIdLocation4(type: String, master_id: Int): LiveData<List<NearbyPlaces>> {
        val a = mRepositoryPlace.getByIdLocation41(type, master_id)
        mAllDataSchool.postValue(a)
        return mAllDataForLoc4 }


}