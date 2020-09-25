package com.sofianem.realestatemanager.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sofianem.realestatemanager.data.model.NearbyPlaces


@Dao
interface PlaceDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveLocation(np: NearbyPlaces)

    @Query("SELECT  * FROM Places where  Place_type like :type  AND Place_master_id like :master_id ORDER BY Place_distance ASC")
    fun getByIdLocation(type: String, master_id: Int): LiveData<List<NearbyPlaces>>


}