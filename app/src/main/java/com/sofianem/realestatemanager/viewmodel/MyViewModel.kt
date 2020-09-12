package com.sofianem.realestatemanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.sofianem.realestatemanager.data.model.EstateR
import com.sofianem.realestatemanager.data.repository.EstateRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MyViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository: EstateRepo = EstateRepo(application)
    lateinit var location: String
    var mNbPhoto = 0
    val allWords: LiveData<List<EstateR>>

    init { allWords = mRepository.readAll }

    fun insertTodo(type: String, city: String, price: Int, surface: Int, number_of_room: Int, description: String, adress: String, location: String,
                       status: String, date_begin: Long, date_end: Long, personn: String, imageUri: MutableList<String?>,
                       imageDescription: MutableList<String?>){
        mNbPhoto = imageUri.size
        val db = EstateR()
        db.type = type ; db.price = price ; db.city = city; db.surface = surface ; db.number_of_room = number_of_room
        db.description = description ; db.adress = adress ; db.location = location ; db.status = status
        db.date_begin = date_begin ; db.date_end = date_end ; db.personn = personn ; db.nb_photo = mNbPhoto
        db.ImageUri = imageUri ; db.ImageDescription = imageDescription

           mRepository.insertTodo(db)
    }

    fun saveIdData(it: List<Int>): ArrayList<EstateR> { return mRepository.readByID(it) }

    fun updateTodo(todo: EstateR) { mRepository.updateTodo(todo) }

    fun getSearchAll(
        personn: String?, type: String?, surfaceMini: Int?, surfaceMax: Int?, priceMini: Int?, priceMax: Int?, roomMini: Int?, roomMax: Int?, dateCreateBegin: Long?, dateCreateEnd: Long?, nb_photo_mini:Int?, nb_photo_max:Int?, dateSoldBegin: Long?, dateSoldBeginEnd: Long?, status: String?, pharmacy:String?, school:String?, market:String?, park:String? ):  List<Int>? {
    return  mRepository.getSearchAll(
        personn, type,surfaceMini,surfaceMax,priceMini,priceMax,roomMini,roomMax,dateCreateBegin,dateCreateEnd,nb_photo_mini,nb_photo_max,dateSoldBegin,dateSoldBeginEnd,status,pharmacy,school,market,park)
    }

    fun UpdateNbPhoto(nb_photo: Int, id: Int) { mRepository.updateNbPhoto(nb_photo, id) }

    fun UpdateProxPharma(pharmacy: String, id: Int) { mRepository.updateProxPharma(pharmacy, id) }

    fun UpdateProxPark(park: String, id: Int) {mRepository.updateProxPark(park, id) }

    fun UpdateProxSchool(school: String, id: Int) {mRepository.updateProxSchool(school, id) }

    fun UpdateProxMarket(market: String, id: Int) {mRepository.updateProxMarket(market, id) }

}
