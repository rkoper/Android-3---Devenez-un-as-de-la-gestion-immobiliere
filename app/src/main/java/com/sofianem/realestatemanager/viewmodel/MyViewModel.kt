package com.sofianem.realestatemanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sofianem.realestatemanager.data.dataBase.ImageDatabase
import com.sofianem.realestatemanager.data.model.EstateR
import com.sofianem.realestatemanager.data.repository.EstateRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MyViewModel(application: Application) : AndroidViewModel(application) {
    private val mImageDb: ImageDatabase? = ImageDatabase.getInstance(application)
    private val mAllData = MutableLiveData<List<EstateR>>()
    private var mAllDataForSearch: List<Int>? = arrayListOf()
    private var mDataSearchList: ArrayList<EstateR> = arrayListOf()
    private val mRepository: EstateRepo = EstateRepo(application)
    var mId: Int = 0
    lateinit var location: String
    var mNbPhoto = 0

    val allWords: LiveData<List<EstateR>>

    init {
        allWords = mRepository.readAll
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

    fun insertTodo(type: String, city: String, price: Int, surface: Int, number_of_room: Int, description: String, adress: String, location: String,
                       status: String, date_begin: Long, date_end: Long, personn: String, imageUri: MutableList<String?>,
                       imageDescription: MutableList<String?>){
        mNbPhoto = imageUri.size
        val db = EstateR()
        db.type = type ; db.price = price ; db.city = city; db.surface = surface ; db.number_of_room = number_of_room
        db.description = description ; db.adress = adress ; db.location = location ; db.status = status
        db.date_begin = date_begin ; db.date_end = date_end ; db.personn = personn ; db.nb_photo = mNbPhoto
        db.ImageUri = imageUri ; db.ImageDescription = imageDescription

        GlobalScope.launch {
           mRepository.insertTodo(db)
        }
    }

    fun retrieveData(): LiveData<List<EstateR>> {
        GlobalScope.launch {
            val list = mImageDb?.estateDao()?.getAll()
            mAllData.postValue(list) }
        return mAllData }


    //



    fun saveIdData(it: List<Int>): ArrayList<EstateR> {
        it.forEach {
            GlobalScope.launch {
                mDataSearchList.add(
                    mImageDb!!.estateDao().getById(it)) } }
        runBlocking { delay(2000) }; return mDataSearchList }



    fun updateTodo(todo: EstateR) {
        mRepository.updateTodo(todo)
    }



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
