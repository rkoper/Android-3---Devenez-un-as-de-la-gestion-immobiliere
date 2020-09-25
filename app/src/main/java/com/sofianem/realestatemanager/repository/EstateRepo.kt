package com.sofianem.realestatemanager.repository

import androidx.lifecycle.LiveData
import com.sofianem.realestatemanager.data.dao.EstateDao
import com.sofianem.realestatemanager.data.model.EstateR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

open class EstateRepo (private val estate_Dao: EstateDao) {


    var readAllLive: LiveData<List<EstateR>> = estate_Dao.getAll()
    var readAllId: LiveData<List<Int>> = estate_Dao.getAllId()

    fun updateTodo(todo: EstateR) {
        GlobalScope.launch(Dispatchers.IO) {
            estate_Dao.update(todo)
        }
    }

    fun insertTodo(todo: EstateR): Long {
        return estate_Dao.insert(todo)
    }


    fun getSearchAll(
        personn: String?,
        type: String?,
        surfaceMini: Int?, surfaceMax: Int?,
        priceMini: Int?, priceMax: Int?,
        roomMini: Int?, roomMax: Int?,
        dateCreateBegin: Long?, dateCreateEnd: Long?,
        nb_photo_mini: Int?, nb_photo_max: Int?,
        dateSoldBegin: Long?, dateSoldBeginEnd: Long?,
        status: String?,
        pharmacy: String?, school: String?, market: String?, park: String?
    ): LiveData<List<Int>> =

       estate_Dao.getSearchAll(
            personn,
            type,
            surfaceMini, surfaceMax,
            priceMini, priceMax,
            roomMini, roomMax,
            dateCreateBegin, dateCreateEnd,
            nb_photo_mini, nb_photo_max,
            dateSoldBegin, dateSoldBeginEnd,
            status,
            pharmacy, school, market, park)

    fun updateProxPharma(pharmacy: String, id: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            estate_Dao.updateProxPharma(pharmacy, id)
        }
    }

    fun updateProxPark(park: String, id: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            estate_Dao.updateProxPark(park, id)
        }
    }

    fun updateProxSchool(school: String, id: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            estate_Dao.updateProxSchool(school, id)
        }
    }

    fun updateProxMarket(market: String, id: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            estate_Dao.updateProxMarket(market, id)
        }
    }

    fun getById(mId: Int): LiveData<EstateR> {
        return estate_Dao.getById(mId)
    }

    fun getGeoLocById(mId: Int): String {
        return estate_Dao.getGeoLocById(mId)
    }


}



