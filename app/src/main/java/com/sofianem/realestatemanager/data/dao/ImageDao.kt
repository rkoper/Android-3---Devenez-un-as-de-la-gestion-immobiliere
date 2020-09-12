package com.sofianem.realestatemanager.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sofianem.realestatemanager.data.model.ImageV


@Dao
interface ImageDao {

    @Query("SELECT * FROM Image")
    fun getImageAll(): LiveData<List<ImageV>>

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

 //   @Query("DELETE FROM Image where Image_uri like :path")
   // fun getByPath1(path: String): ImageV

   // @Query("UPDATE Image SET Image_uri = :path")
  //  fun getByPath2(path: String): ImageV
}
