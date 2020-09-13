package com.sofianem.realestatemanager.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.sofianem.realestatemanager.data.dao.EstateDao
import com.sofianem.realestatemanager.data.dataBase.AllDatabase
import com.sofianem.realestatemanager.data.model.EstateR
import com.sofianem.realestatemanager.data.model.ImageV
import com.sofianem.realestatemanager.data.model.NearbyPlaces
import kotlinx.coroutines.*

open class EstateRepo (application: Application) {

    private val estate_Dao: EstateDao
    private var mDataSearchList: ArrayList<EstateR> = arrayListOf()
    private var mAllDataForSearch: List<Int>? = arrayListOf()
    var readAllLive: LiveData<List<EstateR>>

    init {
        val database = AllDatabase.getInstance(application.applicationContext)
        estate_Dao = database!!.estateDao()
       readAllLive = estate_Dao.getAllLiveList()
    }

    fun updateTodo(todo: EstateR) = runBlocking {
        this.launch(Dispatchers.IO) {
            estate_Dao.update(todo)
        }
    }


        fun insertTodo(todo: EstateR) = runBlocking {
            this.launch(Dispatchers.IO) {
                estate_Dao.insert(todo)
            }
        }


        fun readByID(listId: List<Int>): ArrayList<EstateR> = runBlocking {
            listId.forEach { it ->
                GlobalScope.launch(Dispatchers.IO) { mDataSearchList.add(estate_Dao.getById(it)) }
            }
            return@runBlocking mDataSearchList
        }

        fun readWithID(id: Int): EstateR = runBlocking {
            var listEstate = EstateR()
            this.launch(Dispatchers.IO) {  listEstate = estate_Dao.readWithID(id) }
            println(" ----    listEstate ------ " + listEstate)
            return@runBlocking listEstate
        }


        fun getSearchAll( personn: String?, type: String?, surfaceMini: Int?,
                          surfaceMax: Int?,
                          priceMini: Int?,
                          priceMax: Int?,
                          roomMini: Int?,
                          roomMax: Int?,
                          dateCreateBegin: Long?,
                          dateCreateEnd: Long?,
                          nb_photo_mini: Int?,
                          nb_photo_max: Int?,
                          dateSoldBegin: Long?,
                          dateSoldBeginEnd: Long?,
                          status: String?,
                          pharmacy: String?,
                          school: String?,
                          market: String?,
                          park: String?
        ): List<Int>? = runBlocking {

            mAllDataForSearch = estate_Dao.getSearchAll(
                personn,
                type,
                surfaceMini,
                surfaceMax,
                priceMini,
                priceMax,
                roomMini,
                roomMax,
                dateCreateBegin,
                dateCreateEnd,
                nb_photo_mini,
                nb_photo_max,
                dateSoldBegin,
                dateSoldBeginEnd,
                status,
                pharmacy,
                school,
                market,
                park
            )
            return@runBlocking mAllDataForSearch
        }


        fun updateNbPhoto(nb_photo: Int, id: Int) = runBlocking {
            this.launch(Dispatchers.IO) {
                estate_Dao.updateNbPhoto(nb_photo, id)
            }
        }

        fun updateProxPharma(pharmacy: String, id: Int) = runBlocking {
            this.launch(Dispatchers.IO) {
                estate_Dao.updateProxPharma(pharmacy, id)
            }
        }

        fun updateProxPark(park: String, id: Int) = runBlocking {
            this.launch(Dispatchers.IO) {
                estate_Dao.updateProxPark(park, id)
            }
        }

        fun updateProxSchool(school: String, id: Int) = runBlocking {
            this.launch(Dispatchers.IO) {
                estate_Dao.updateProxSchool(school, id)
            }
        }

        fun updateProxMarket(market: String, id: Int) = runBlocking {
            this.launch(Dispatchers.IO) {
                estate_Dao.updateProxMarket(market, id)
            }
        }
    }



