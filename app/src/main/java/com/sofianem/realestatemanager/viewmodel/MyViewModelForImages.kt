package com.sofianem.realestatemanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.sofianem.realestatemanager.data.model.*
import com.sofianem.realestatemanager.data.repository.ImageRepo
import kotlinx.coroutines.runBlocking

class MyViewModelForImages(application: Application) : AndroidViewModel(application) {
    private val mRepositoryImage: ImageRepo = ImageRepo(application)
    val allImageLive: LiveData<List<ImageV>>

    init { allImageLive = mRepositoryImage.readAllImageLive }

     fun storeImageData(mId: Int, listImage: MutableList<String?>, listImageDescription: MutableList<String?>) {
         mRepositoryImage.insertImage(mId, listImage, listImageDescription)}

    fun upadeSingleImageData(mId: Int, stringImage: String?, stringeDescription: String?) {
        mRepositoryImage.insertItem(mId, stringImage,stringeDescription )}

    fun deleteImageById(i: Int) { mRepositoryImage.DeleteImageByID(i) }

  //  fun updateImageDes(ig: ImageV) = runBlocking { mRepositoryImage.UpdateImageDes(ig)}

    fun updateImageDes(ig: ImageV) { mRepositoryImage.UpdateImageDes(ig) }


}