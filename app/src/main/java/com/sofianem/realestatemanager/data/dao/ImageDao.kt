package com.sofianem.realestatemanager.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sofianem.realestatemanager.data.model.ImageV


@Dao
interface ImageDao {


    @Query("SELECT * FROM Image")
    fun getImageAllLive():  LiveData<List<ImageV>>

    @Insert
    fun insertItem(imageV: ImageV): Long

    @Update
    fun updateItem(imageV: ImageV)

    @Query("DELETE FROM Image where Image_id like :mDeleteId")
    fun deleteById(mDeleteId: Int)

    @Query("SELECT * FROM Image where Image_master_id like :id")
    fun getById(id: Int):  LiveData<ImageV>

}
