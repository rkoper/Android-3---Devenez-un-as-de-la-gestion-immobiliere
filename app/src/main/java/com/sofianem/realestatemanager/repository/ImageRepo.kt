package com.sofianem.realestatemanager.repository

import androidx.lifecycle.LiveData
import com.sofianem.realestatemanager.data.dao.ImageDao
import com.sofianem.realestatemanager.data.model.ImageV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

open class ImageRepo (image_Dao: ImageDao) {
    val image_Dao = image_Dao
    var readAllImageLive: LiveData<List<ImageV>> = image_Dao.getImageAllLive()


    fun insertImage(mId: Int, listImage: MutableList<String?>, listImageDescription: MutableList<String?>) {
        val i = ImageV()
        GlobalScope.launch(Dispatchers.IO) {
            for (n in listImage.indices) {
                i.imageUri = listImage[n]
                i.imageDescription = listImageDescription[n]
                i.masterId = mId
                image_Dao.insertItem(i)
            } } }

   fun insertItem(mId: Int, stringImage: String?, stringeDescription: String?) {
    val i = ImageV()
       GlobalScope.launch(Dispatchers.IO) {
        i.imageUri = stringImage
        i.imageDescription = stringeDescription
        i.masterId = mId
        image_Dao.insertItem(i) } }

    fun UpdateImageDes(ig: ImageV) {
        GlobalScope.launch(Dispatchers.IO) {
            image_Dao.updateItem(ig)
        }
    }

    fun DeleteImageByID(i: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            image_Dao.deleteById(i)
        }
    }

    fun getById(mId: Int): LiveData<ImageV> {
        return image_Dao.getById(mId)
    }
}

