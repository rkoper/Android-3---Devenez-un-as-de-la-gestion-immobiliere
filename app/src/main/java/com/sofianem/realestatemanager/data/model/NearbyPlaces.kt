package com.sofianem.realestatemanager.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Places", foreignKeys = [ForeignKey(
        entity = (EstateR::class),
        parentColumns = arrayOf("Estate_id"),
        childColumns = arrayOf("Place_master_id")
    )]
)

data class NearbyPlaces(

    @PrimaryKey(autoGenerate = true)
    var placeId: Int? = null,
    @ColumnInfo(name = "Place_location")
    var placeLocation: String = "",
    @ColumnInfo(name = "Place_name")
    var placeName: String = "",
    @ColumnInfo(name = "Place_master_id")
    var placeMasterId: Int = 0,
    @ColumnInfo(name = "Place_distance")
    var placeDistance: Int = 0,
    @ColumnInfo(name = "Place_type")
    var placetype: String = ""


)
