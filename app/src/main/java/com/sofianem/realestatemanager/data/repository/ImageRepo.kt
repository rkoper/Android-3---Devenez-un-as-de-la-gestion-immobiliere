package com.sofianem.realestatemanager.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.sofianem.realestatemanager.data.dao.ImageDao
import com.sofianem.realestatemanager.data.dataBase.AllDatabase
import com.sofianem.realestatemanager.data.model.ImageV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

open class ImageRepo (application: Application) {

    val image_Dao: ImageDao
    var readAllImageLive: LiveData<List<ImageV>>

    init { val database = AllDatabase.getInstance(application.applicationContext)
        image_Dao = database!!.imageDao()
        readAllImageLive = image_Dao.getImageAllLive() }



    fun insertImage(mId: Int, listImage: MutableList<String?>, listImageDescription: MutableList<String?>) = runBlocking {
        val i = ImageV()
        this.launch(Dispatchers.IO) {
            for (n in listImage.indices) {
                i.imageUri = listImage[n]
                i.imageDescription = listImageDescription[n]
                i.masterId = mId
                image_Dao.insertItem(i)
            } } }


   fun insertItem(mId: Int, stringImage: String?, stringeDescription: String?) = runBlocking {
    val i = ImageV()
    this.launch(Dispatchers.IO) {
        i.imageUri = stringImage
        i.imageDescription = stringeDescription
        i.masterId = mId
        image_Dao.insertItem(i) } }


    fun readImageByID(i: Int): List<ImageV> = runBlocking {
        var igl: List<ImageV> = arrayListOf()
            GlobalScope.launch(Dispatchers.IO) { igl = image_Dao.retriedImageryMasterID(i) }
        return@runBlocking igl }

  //  fun getByPath1(s: String) = runBlocking {
    //    GlobalScope.launch(Dispatchers.IO) { image_Dao.getByPath1(s) } }

  //  fun getByPath2(s: String) = runBlocking {
  //      GlobalScope.launch(Dispatchers.IO) { image_Dao.getByPath2(s) } }

    fun getByPath(s: String) : ImageV = runBlocking {
        var a = ImageV()
        GlobalScope.launch(Dispatchers.IO) {  a = image_Dao.getByPath(s) }
        return@runBlocking a
    }

    fun deleteImage(ig: ImageV) = runBlocking {
        GlobalScope.launch(Dispatchers.IO) { image_Dao.deleteItem(ig) } }

    fun UpdateImageDes(ig: ImageV) = runBlocking {
        this.launch(Dispatchers.IO) {
            image_Dao.updateItem(ig)
        }
    }

}

