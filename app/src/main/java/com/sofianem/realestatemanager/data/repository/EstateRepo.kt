package com.sofianem.realestatemanager.data.repository

import android.app.Application
import android.database.Cursor
import androidx.lifecycle.LiveData
import com.sofianem.realestatemanager.data.dao.EstateDao
import com.sofianem.realestatemanager.data.dataBase.ImageDatabase
import com.sofianem.realestatemanager.data.model.EstateR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

open class EstateRepo (application: Application) {

    val estate_Dao: EstateDao
    var readAll: LiveData<List<EstateR>>

    init {
        val database = ImageDatabase.getInstance(application.applicationContext)
        estate_Dao = database!!.estateDao()
        readAll = estate_Dao.getAllTodoList()
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



}
