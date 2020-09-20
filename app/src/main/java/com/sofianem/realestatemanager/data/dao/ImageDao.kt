package com.sofianem.realestatemanager.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sofianem.realestatemanager.data.model.EstateR
import com.sofianem.realestatemanager.data.model.ImageV


@Dao
interface ImageDao {


    @Query("SELECT * FROM Image")
    fun getImageAllLive():  LiveData<List<ImageV>>

    @Insert
    fun insertItem(imageV: ImageV): Long

    @Update
    fun updateItem(imageV: ImageV)

    @Delete
    fun deleteItem (ImageV: ImageV)

    @Query("DELETE FROM Image where Image_id like :mDeleteId")
    fun deleteById(mDeleteId: Int)

    @Query("SELECT * FROM Image where Image_master_id like :id")
    fun getById(id: Int):  LiveData<ImageV>

}
