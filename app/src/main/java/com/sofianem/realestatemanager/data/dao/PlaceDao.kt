package com.sofianem.realestatemanager.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sofianem.realestatemanager.data.model.EstateR
import com.sofianem.realestatemanager.data.model.NearbyPlaces


@Dao
interface PlaceDao {

    @Query("SELECT * FROM Places")
    fun getPlaceAll(): LiveData<List<NearbyPlaces>>
    @Insert
    fun insertLoc1(np: NearbyPlaces)
    @Insert
    fun insertLoc2(np: NearbyPlaces)
    @Insert
    fun insertLoc3(np: NearbyPlaces)
    @Insert
    fun insertLoc4(np: NearbyPlaces)

    @Query("SELECT  * FROM Places where  Place_type like :type  AND Place_master_id like :master_id ORDER BY Place_distance ASC")
    fun getByIdLocation(type: String, master_id: Int): LiveData<List<NearbyPlaces>>


}