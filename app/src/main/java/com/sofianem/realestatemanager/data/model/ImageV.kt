package com.sofianem.realestatemanager.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Image", foreignKeys = [ForeignKey(
        entity = (EstateR::class),
        parentColumns = arrayOf("Estate_id"),
        childColumns = arrayOf("Image_master_id")
    )]
)

data class ImageV(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Image_id")
    var imageId: Int? = null,
    @ColumnInfo(name = "Image_uri")
    var imageUri: String? = "",
    @ColumnInfo(name = "Image_master_id")
    var masterId: Int = 0,
    @ColumnInfo(name = "Image_description")
    var imageDescription: String? = ""
)