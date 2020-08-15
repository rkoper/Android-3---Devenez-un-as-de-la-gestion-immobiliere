package com.sofianem.realestatemanager.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import android.content.ContentValues as ContentValues1

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
) {


    fun fromContentValues(values: ContentValues1?): ImageV {
        val image = ImageV()
        if (values != null) {
            if (values.containsKey("Image_id")) image.imageId = values.getAsInteger("Image_id")
            if (values.containsKey("Image_uri")) image.imageUri = values.getAsString("Image_uri")
            if (values.containsKey("Image_master_id")) image.masterId = values.getAsInteger("Image_master_id")
            if (values.containsKey("Image_description")) image.imageDescription = values.getAsString("Image_description")
        }
        return image
    }


}
