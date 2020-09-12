package com.sofianem.realestatemanager.viewmodel

import android.app.Application
import android.media.Image
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sofianem.realestatemanager.data.dataBase.ImageDatabase
import com.sofianem.realestatemanager.data.model.*
import com.sofianem.realestatemanager.data.repository.EstateRepo
import com.sofianem.realestatemanager.data.repository.ImageRepo
import com.sofianem.realestatemanager.services.MapService
import com.sofianem.realestatemanager.utils.Utils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt

class MyViewModelForImages(application: Application) : AndroidViewModel(application) {
    private val mRepositoryImage: ImageRepo = ImageRepo(application)
    val allImage: LiveData<List<ImageV>>

    init { allImage = mRepositoryImage.readAllImage }

     fun storeImageData(mId: Int, listImage: MutableList<String?>, listImageDescription: MutableList<String?>) {
         mRepositoryImage.insertImage(mId, listImage, listImageDescription)}

    fun upadeSingleImageData(mId: Int, stringImage: String?, stringeDescription: String?) {
        mRepositoryImage.insertItem(mId, stringImage,stringeDescription )}

    fun retrievImagebyMasterID(it: Int): List<ImageV> { return mRepositoryImage.readImageByID(it) }

    fun getByPath(it: String) : ImageV {  return  mRepositoryImage.getByPath(it) }

    // delete by path
 //   fun getByPath1(it: String) {  mRepositoryImage.getByPath1(it) }

    // update by path
  //  fun getByPath2(it: String) {  mRepositoryImage.getByPath2(it) }

    fun deleteImage(ig: ImageV) { mRepositoryImage.deleteImage(ig) }

    fun UpdateImageDes(ig: ImageV) { mRepositoryImage.UpdateImageDes(ig) }

}