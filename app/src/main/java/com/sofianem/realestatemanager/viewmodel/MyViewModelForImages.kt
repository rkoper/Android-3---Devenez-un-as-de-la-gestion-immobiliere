package com.sofianem.realestatemanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sofianem.realestatemanager.data.dataBase.ImageDatabase
import com.sofianem.realestatemanager.data.model.*
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
    private val mImageDb: ImageDatabase? = ImageDatabase.getInstance(application)
    private val mAllImageData = MutableLiveData<List<ImageV>>()

     fun storeImageData(
        mId: Int,
        listImage: MutableList<String?>,
        listImageDescription: MutableList<String?>
    ) { val i = ImageV()
        GlobalScope.launch {
            for (n in listImage.indices) {
                i.imageUri = listImage[n]
                i.imageDescription = listImageDescription[n]
                i.masterId = mId
                mImageDb?.imageDao()?.insertItem(i)
            } } }

    fun upadeSingleImageData(mId: Int, stringImage: String?, stringeDescription: String?) {
        val i = ImageV()
        GlobalScope.launch {
            i.imageUri = stringImage
            i.imageDescription = stringeDescription
            i.masterId = mId
            mImageDb?.imageDao()?.insertItem(i)
        } }

    fun retrieveImageData(): LiveData<List<ImageV>> {
        GlobalScope.launch {
            val listImage = mImageDb?.imageDao()?.getImageAll()
            mAllImageData.postValue(listImage) }
        return mAllImageData }

    fun retrievImagebyMasterID(it: Int): List<ImageV> {
        var igl: List<ImageV> = arrayListOf()
        GlobalScope.launch { igl = mImageDb!!.imageDao().retriedImageryMasterID(it) }
        runBlocking { delay(100) }; return igl }

    fun retrievebyPath(it: String): ImageV {
        var ig = ImageV()
        GlobalScope.launch { ig = mImageDb!!.imageDao().getByPath(it) }
        runBlocking { delay(100) }; return ig }

    fun deleteImage(ig: ImageV) {
        GlobalScope.launch { mImageDb?.imageDao()?.deleteItem(ig) }
    }

    fun UpdateNbPhoto(nb_photo: Int, id: Int) {
        GlobalScope.launch { mImageDb?.estateDao()?.updateNbPhoto(nb_photo, id) }
    }


    fun UpdateImageDes(ig: ImageV) {
        GlobalScope.launch { mImageDb?.imageDao()?.updateItem(ig) }
    }

}