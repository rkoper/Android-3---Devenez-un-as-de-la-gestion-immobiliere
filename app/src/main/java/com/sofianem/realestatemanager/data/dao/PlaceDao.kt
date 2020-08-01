package com.sofianem.realestatemanager.data.Dao

import androidx.room.*
import com.sofianem.realestatemanager.data.Model.NearbyPlaces


@Dao
interface PlaceDao {

    @Query("SELECT * FROM Places")
    fun getPlaceAll(): List<NearbyPlaces>

    @Insert
    fun insertLoc1(np: NearbyPlaces)
    @Insert
    fun insertLoc2(np: NearbyPlaces)
    @Insert
    fun insertLoc3(np: NearbyPlaces)
    @Insert
    fun insertLoc4(np: NearbyPlaces)

    @Update
    fun updateItem(np: NearbyPlaces)

    @Delete
    fun deleteItem(np: NearbyPlaces)

    @Query("SELECT Place_master_id FROM Places")
    fun getLocationId(): List<Int>


    @Query("SELECT  * FROM Places where  Place_type like :type  AND Place_master_id like :master_id ORDER BY Place_distance ASC")
    fun getByIdLocation(type: String, master_id: Int): MutableList<NearbyPlaces>
    @Query("SELECT  * FROM Places where  Place_type like :type  AND Place_master_id like :master_id ORDER BY Place_distance ASC")
    fun getByIdLocation1(type: String, master_id: Int): MutableList<NearbyPlaces>
    @Query("SELECT  * FROM Places where  Place_type like :type  AND Place_master_id like :master_id ORDER BY Place_distance ASC")
    fun getByIdLocation2(type: String, master_id: Int): MutableList<NearbyPlaces>
    @Query("SELECT  * FROM Places where  Place_type like :type  AND Place_master_id like :master_id ORDER BY Place_distance ASC")
    fun getByIdLocation3(type: String, master_id: Int): MutableList<NearbyPlaces>
    @Query("SELECT  * FROM Places where  Place_type like :type  AND Place_master_id like :master_id ORDER BY Place_distance ASC")
    fun getByIdLocation4(type: String, master_id: Int): MutableList<NearbyPlaces>
    @Query("SELECT  * FROM Places where Place_master_id like :master_id")
    fun getByIdLocationAll(master_id: Int): MutableList<NearbyPlaces>

}