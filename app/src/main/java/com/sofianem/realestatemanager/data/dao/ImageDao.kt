package com.sofianem.realestatemanager.data.dao

import androidx.room.*
import com.sofianem.realestatemanager.data.model.ImageV


@Dao
interface ImageDao {

    @Query("SELECT * FROM Image")
    fun getImageAll(): List<ImageV>

    @Insert
    fun insertItem(imageV: ImageV): Long

    @Update
    fun updateItem(imageV: ImageV)

    @Delete
    fun deleteItem (ImageV: ImageV)

    @Query("SELECT * FROM Image where Image_master_id like :id")
    fun retriedImageryMasterID(id: Int):  List<ImageV>

    @Query("SELECT * FROM Image where Image_uri like :path")
    fun getByPath(path: String): ImageV

}
