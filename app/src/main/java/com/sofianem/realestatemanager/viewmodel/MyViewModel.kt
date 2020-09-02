package com.sofianem.realestatemanager.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sofianem.realestatemanager.data.DataBase.EstateDatabase
import com.sofianem.realestatemanager.data.DataBase.ImageDatabase
import com.sofianem.realestatemanager.data.repository.EstateRepo
import com.sofianem.realestatemanager.data.model.*
import com.sofianem.realestatemanager.services.MapService
import com.sofianem.realestatemanager.utils.GeocoderUtil
import com.sofianem.realestatemanager.utils.Utils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class MyViewModel(application: Application) : AndroidViewModel(application) {
    private val mImageDb: ImageDatabase? = ImageDatabase.getInstance(application)
    private val mAllData = MutableLiveData<List<EstateR>>()
    private val mAllDatav2 = MutableLiveData<List<EstateR>>()
    private val mIdLoc = MutableLiveData<List<Int>>()
    private var mAllDataForSearch: List<Int>? = arrayListOf()
    private val mAllImageData = MutableLiveData<List<ImageV>>()
    private var mDataSearchList: ArrayList<EstateR> = arrayListOf()
    private val mRepository: EstateRepo = EstateRepo(application)
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
    var mId: Int = 0
    lateinit var location: String
    var mNbPhoto = 0

    val allWords: LiveData<List<EstateR>>

    init {
        allWords = mRepository.estate_Dao.getAllTodoList()
    }

    fun storeData(
        type: String, city: String, price: Int, surface: Int, number_of_room: Int, description: String, adress: String, location: String,
        status: String, date_begin: Long, date_end: Long, personn: String, imageUri: MutableList<String?>,
        imageDescription: MutableList<String?>
    ): Int {
        mNbPhoto = imageUri.size
        val db = EstateR()
        db.type = type ; db.price = price ; db.city = city; db.surface = surface ; db.number_of_room = number_of_room
        db.description = description ; db.adress = adress ; db.location = location ; db.status = status
        db.date_begin = date_begin ; db.date_end = date_end ; db.personn = personn ; db.nb_photo = mNbPhoto
        db.ImageUri = imageUri ; db.ImageDescription = imageDescription

        GlobalScope.launch {
            mId = mImageDb?.estateDao()?.insert(db)?.toInt()!!
            //     location = GeocoderUtil.getlocationForList(mId, adress, city, mContext)
            //   db.location = location
            //  findLocation(mId!!.toInt(), adress, city, db, mContext)
            //storeImageData(mId, imageUri, imageDescription) }
            // runBlocking { delay(900) }
        }
        return mId
    }

    fun findLocation(
        id: Int, adress: String?, city: String?, db: EstateR, mContext: Context
    ): String {
     //   location = GeocoderUtil.getlocationForList(id, adress, city, mContext)
       // db.location = location
        GlobalScope.launch {
          //  mImageDb?.estateDao()?.updateForLoc(location, id)
            getNearbyPlace1(mId, location) ; getNearbyPlace2(mId, location)
            getNearbyPlace3(mId, location) ; getNearbyPlace4(mId, location)}
        return location
    }

    private fun storeImageData(
        mId: Int,
        listImage: MutableList<String?>,
        listImageDescription: MutableList<String?>
    ) { val i = ImageV()
        GlobalScope.launch {
            for (n in listImage.indices) {
                i.imageUri = listImage[n]
                i.imageDescription = listImageDescription[n]
                i.masterId = mId
                mImageDb?.imageDao()?.insertItem(i)
            } } }

    fun upadeSingleImageData(mId: Int, stringImage: String?, stringeDescription: String?) {
        val i = ImageV()
        GlobalScope.launch {
            i.imageUri = stringImage
            i.imageDescription = stringeDescription
            i.masterId = mId
            mImageDb?.imageDao()?.insertItem(i)
        } }


    fun retrieveData(): LiveData<List<EstateR>> {
        GlobalScope.launch {
            val list = mImageDb?.estateDao()?.getAll()
            mAllData.postValue(list) }
        return mAllData }


    fun retrieveImageData(): LiveData<List<ImageV>> {
        GlobalScope.launch {
            val listImage = mImageDb?.imageDao()?.getImageAll()
            mAllImageData.postValue(listImage) }
        return mAllImageData }

    fun saveIdData(it: List<Int>): ArrayList<EstateR> {
        it.forEach {
            GlobalScope.launch {
                mDataSearchList.add(
                    mImageDb!!.estateDao().getById(it)) } }
        runBlocking { delay(2000) }; return mDataSearchList }

    fun retrievImagebyMasterID(it: Int): List<ImageV> {
        var igl: List<ImageV> = arrayListOf()
        GlobalScope.launch { igl = mImageDb!!.imageDao().retriedImageryMasterID(it) }
        runBlocking { delay(100) }; return igl }

    fun retrievebyPath(it: String): ImageV {
        var ig = ImageV()
        GlobalScope.launch { ig = mImageDb!!.imageDao().getByPath(it) }
        runBlocking { delay(100) }; return ig }

    fun updateTodo(todo: EstateR) {
        mRepository.updateTodo(todo)
    }

    fun deleteImage(ig: ImageV) {
        GlobalScope.launch { mImageDb?.imageDao()?.deleteItem(ig) }
    }

    fun UpdateNbPhoto(nb_photo: Int, id: Int) {
        GlobalScope.launch { mImageDb?.estateDao()?.updateNbPhoto(nb_photo, id) }
    }

    fun UpdateProxPharma(pharmacy: String, id: Int) {
        GlobalScope.launch { mImageDb?.estateDao()?.updateProxPharma(pharmacy, id) }
    }

    fun UpdateProxPark(park: String, id: Int) {
        GlobalScope.launch { mImageDb?.estateDao()?.updateForPark(park, id) }
    }

    fun UpdateProxSchool(school: String, id: Int) {
        GlobalScope.launch { mImageDb?.estateDao()?.updateProxSchool(school, id) }
    }

    fun UpdateProxMarket(market: String, id: Int) {
        GlobalScope.launch { mImageDb?.estateDao()?.updateProxMarket(market, id) }
    }

    fun UpdateImageDes(ig: ImageV) {
        GlobalScope.launch { mImageDb?.imageDao()?.updateItem(ig) }
    }

     fun getNearbyPlace1(id: Int, location: String) {
        var interceptor = HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build();
        var retrofit: Retrofit = Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/api/place/").addConverterFactory(GsonConverterFactory.create()).client(client).build();
        val service = retrofit.create(MapService::class.java)
        var call = service.getNearbyPlaces1(location, "pharmacy")
        call.enqueue(object : Callback<PlacesResponse1> { override fun onFailure(call: Call<PlacesResponse1>, t: Throwable) {}
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

    private fun getNearbyPlace2(id: Int, location: String) {
        var interceptor = HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build();
        var retrofit: Retrofit = Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/api/place/").addConverterFactory(GsonConverterFactory.create()).client(client).build();
        val service = retrofit.create(MapService::class.java)
        var call = service.getNearbyPlaces2(location, "park")
        call.enqueue(object : Callback<PlacesResponse2> { override fun onFailure(call: Call<PlacesResponse2>, t: Throwable) {}
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

    private fun getNearbyPlace3(id: Int, location: String) {
        var interceptor = HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build();
        var retrofit: Retrofit = Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/api/place/").addConverterFactory(GsonConverterFactory.create()).client(client).build();
        val service = retrofit.create(MapService::class.java)
        var call = service.getNearbyPlaces3(location, "primary_school")
        call.enqueue(object : Callback<PlacesResponse3> { override fun onFailure(call: Call<PlacesResponse3>, t: Throwable) {}
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

    private fun getNearbyPlace4(id: Int, location: String) {
        var interceptor = HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build();
        var retrofit: Retrofit = Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/api/place/").addConverterFactory(GsonConverterFactory.create()).client(client).build();
        val service = retrofit.create(MapService::class.java)
        var call = service.getNearbyPlaces4(location, "supermarket")
        call.enqueue(object : Callback<PlacesResponse4> { override fun onFailure(call: Call<PlacesResponse4>, t: Throwable) {}
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
        GlobalScope.launch {
            mImageDb?.nearbyPlaceDao()?.insertLoc1(np) } }

    fun saveNearby2(np: NearbyPlaces) {
        GlobalScope.launch {
            mImageDb?.nearbyPlaceDao()?.insertLoc2(np) } }

    fun saveNearby3(np: NearbyPlaces) {
        GlobalScope.launch {
            mImageDb?.nearbyPlaceDao()?.insertLoc3(np) } }

    fun saveNearby4(np: NearbyPlaces) {
        GlobalScope.launch {
            mImageDb?.nearbyPlaceDao()?.insertLoc4(np) } }

    fun getByIdLocationPark(type: String, master_id: Int): LiveData<List<NearbyPlaces>> {
        GlobalScope.launch {
            val a = mImageDb!!.nearbyPlaceDao().getByIdLocation1(type, master_id)
            mAllDataPark.postValue(a) }
        return mAllDataPark }

    fun getByIdLocationSchool(type: String, master_id: Int): LiveData<List<NearbyPlaces>> {
        GlobalScope.launch {
            val a = mImageDb!!.nearbyPlaceDao().getByIdLocation1(type, master_id)
            mAllDataSchool.postValue(a) }
        return mAllDataSchool }

    fun getByIdLocationMarket(type: String, master_id: Int): LiveData<List<NearbyPlaces>> {
        GlobalScope.launch {
            val a = mImageDb!!.nearbyPlaceDao().getByIdLocation1(type, master_id)
            mAllDataMarket.postValue(a) }
        return mAllDataMarket }

    fun getByIdLocationPharmacy(type: String, master_id: Int): LiveData<List<NearbyPlaces>> {
        GlobalScope.launch {
            val a = mImageDb!!.nearbyPlaceDao().getByIdLocation1(type, master_id)
            mAllDataPharmacy.postValue(a) }
        return mAllDataPharmacy }

    fun getByIdLocation1(type: String, master_id: Int): LiveData<List<NearbyPlaces>> {
        GlobalScope.launch {
            val a1 = mImageDb!!.nearbyPlaceDao().getByIdLocation1(type, master_id)
            mAllDataForLoc1.postValue(a1) }
        return mAllDataForLoc1 }

    fun getByIdLocation2(type: String, master_id: Int): LiveData<List<NearbyPlaces>> {
        GlobalScope.launch {
            val a2 = mImageDb!!.nearbyPlaceDao().getByIdLocation2(type, master_id)
            mAllDataForLoc2.postValue(a2) }
        return mAllDataForLoc2 }

    fun getByIdLocation3(type: String, master_id: Int): LiveData<List<NearbyPlaces>> {
        GlobalScope.launch {
            val a3 = mImageDb!!.nearbyPlaceDao().getByIdLocation3(type, master_id)
            mAllDataForLoc3.postValue(a3) }
        return mAllDataForLoc3 }

    fun getByIdLocation4(type: String, master_id: Int): LiveData<List<NearbyPlaces>> {
        GlobalScope.launch {
            val a4 = mImageDb!!.nearbyPlaceDao().getByIdLocation4(type, master_id)
            mAllDataForLoc4.postValue(a4) }
        return mAllDataForLoc4 }

    fun getSearchAll(
        personn: String?,  type: String?, surfaceMini: Int?, surfaceMax: Int?, priceMini: Int?, priceMax: Int?, roomMini: Int?, roomMax: Int?, dateCreateBegin: Long?, dateCreateEnd: Long?,nb_photo_mini:Int?
        , nb_photo_max:Int?,  dateSoldBegin: Long?, dateSoldBeginEnd: Long?, status: String?, pharmacy:String?,school:String?,market:String?,park:String? )
            :  List<Int>? {
        GlobalScope.launch { val list = mImageDb?.estateDao()?.getSearchAll(
            personn, type, surfaceMini, surfaceMax, priceMini, priceMax, roomMini, roomMax, dateCreateBegin, dateCreateEnd, nb_photo_mini, nb_photo_max ,dateSoldBegin, dateSoldBeginEnd, status, pharmacy,school,market,park )
            mAllDataForSearch = list }
        runBlocking { delay(500) }
        return mAllDataForSearch }
}
