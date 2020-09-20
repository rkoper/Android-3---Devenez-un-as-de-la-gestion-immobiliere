package com.sofianem.realestatemanager.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sofianem.realestatemanager.data.model.EstateR
import com.sofianem.realestatemanager.data.model.ImageV


@Dao
interface ImageDao {


    @Query("SELECT * FROM Image")
    fun getImageAllLive():  LiveData<List<ImageV>>

    @Query("SELECT * FROM Image where Image_master_id like :Image_master_id")
    fun getImageById(Image_master_id:Int):  LiveData<ImageV>

    @Query("SELECT * FROM Image where Image_master_id like :Image_master_id")
    fun getImageListById(Image_master_id: Int):  ImageV

    @Insert
    fun insertItem(imageV: ImageV): Long

    @Update
    fun updateItem(imageV: ImageV)

    @Delete
    fun deleteItem (ImageV: ImageV)

    @Query("DELETE FROM Image where Image_id like :mDeleteId")
    fun deleteById(mDeleteId: Int)


}
