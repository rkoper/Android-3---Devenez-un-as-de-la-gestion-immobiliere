package com.sofianem.realestatemanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sofianem.realestatemanager.data.model.ImageV
import com.sofianem.realestatemanager.repository.ImageRepo

class MyViewModelForImages(private val mRepositoryImage : ImageRepo) : ViewModel() {
    val allImageLive: LiveData<List<ImageV>> = mRepositoryImage.readAllImageLive

    fun storeImageData(mId: Int, listImage: MutableList<String?>, listImageDescription: MutableList<String?>) {
         mRepositoryImage.insertImage(mId, listImage, listImageDescription)}

    fun upadeSingleImageData(mId: Int, stringImage: String?, stringeDescription: String?) {
        mRepositoryImage.insertItem(mId, stringImage,stringeDescription )}

    fun deleteImageById(i: Int) { mRepositoryImage.DeleteImageByID(i) }

    fun updateImageDes(ig: ImageV) { mRepositoryImage.UpdateImageDes(ig) }

    fun getById(mId: Int): LiveData<ImageV> {
        return mRepositoryImage.getById( mId)}


}