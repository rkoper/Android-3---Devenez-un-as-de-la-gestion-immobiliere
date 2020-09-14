package com.sofianem.realestatemanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.sofianem.realestatemanager.data.model.*
import com.sofianem.realestatemanager.data.repository.ImageRepo

class MyViewModelForImages(application: Application) : AndroidViewModel(application) {
    private val mRepositoryImage: ImageRepo = ImageRepo(application)
   // val allImage: List<ImageV>
    val allImageLive: LiveData<List<ImageV>>

    init {
        //allImage = mRepositoryImage.readAllImage
        allImageLive = mRepositoryImage.readAllImageLive

    }

     fun storeImageData(mId: Int, listImage: MutableList<String?>, listImageDescription: MutableList<String?>) {
         mRepositoryImage.insertImage(mId, listImage, listImageDescription)}

    fun upadeSingleImageData(mId: Int, stringImage: String?, stringeDescription: String?) {
        mRepositoryImage.insertItem(mId, stringImage,stringeDescription )}

    fun retrievImagebyID(it: Int): ImageV {
        println(" image attribut ------->" + mRepositoryImage.readImageByID(it))
        return mRepositoryImage.readImageByID(it) }

    fun getByPath(it: String) : ImageV {  return  mRepositoryImage.getByPath(it) }

    // delete by path
 //   fun getByPath1(it: String) {  mRepositoryImage.getByPath1(it) }

    // update by path
  //  fun getByPath2(it: String) {  mRepositoryImage.getByPath2(it) }

    fun deleteImage(ig: ImageV) { mRepositoryImage.deleteImage(ig) }

    fun UpdateImageDes(ig: ImageV) { mRepositoryImage.UpdateImageDes(ig) }

}