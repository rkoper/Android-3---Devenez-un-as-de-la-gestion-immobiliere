package com.sofianem.realestatemanager.data.repository

import android.app.Application
import android.database.Cursor
import androidx.lifecycle.LiveData
import com.sofianem.realestatemanager.data.Dao.EstateDao
import com.sofianem.realestatemanager.data.DataBase.ImageDatabase
import com.sofianem.realestatemanager.data.model.EstateR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

open class EstateRepo (application: Application) {

    val estate_Dao: EstateDao
    val allTodos: LiveData<List<EstateR>>

    init {
        val database = ImageDatabase.getInstance(application.applicationContext)
        estate_Dao = database!!.estateDao()
        allTodos = estate_Dao.getAllTodoList()
    }

    fun updateTodo(todo: EstateR) = runBlocking {
        this.launch(Dispatchers.IO) {
            estate_Dao.update(todo)
        }
    }

    fun testSearch(personn:String?, type:String?, surfaceMini: Int?, surfaceMax: Int?, priceMini: Int?, priceMax: Int?, roomMini: Int?, roomMax: Int?, dateCreateBegin: Long?, dateCreateEnd: Long?, nb_photo_mini: Int?, nb_photo_max: Int?, dateSoldBegin: Long?, dateSoldBeginEnd: Long?,status: String?,pharmacy:String?,school:String?,market:String?,park:String? ) {
        GlobalScope.launch(Dispatchers.IO) {
            estate_Dao.getSearchAll(  personn, type, surfaceMini, surfaceMax, priceMini, priceMax, roomMini, roomMax, dateCreateBegin, dateCreateEnd, nb_photo_mini, nb_photo_max ,dateSoldBegin, dateSoldBeginEnd, status, pharmacy,school,market,park)
        }
    }

    fun testrepo(){
        estate_Dao.getEstateWithCursor()
    }

    fun getAllTodoList(): LiveData<List<EstateR>> {
        return allTodos
    }

    fun getEstateWithCursor(): Cursor? =
        estate_Dao.getEstateWithCursor()
}
