package com.sofianem.realestatemanager.data.model

import android.content.ContentValues
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "Estate")
data class EstateR(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Estate_id")
    var id: Int = 0,

    @ColumnInfo(name = "Estate_type")
    var type: String = "-",
    @ColumnInfo(name = "Estate_price")
    var price: Int = 0,
    @ColumnInfo(name = "Estate_city")
    var city: String = "-",
    @ColumnInfo(name = "Estate_surface")
    var surface: Int = 0,
    @ColumnInfo(name = "Estate_number_of_room")
    var number_of_room: Int = 0,
    @ColumnInfo(name = "Estate_description")
    var description: String = "-",
    @ColumnInfo(name = "Estate_adress")
    var adress: String = "-",
    @ColumnInfo(name = "Estate_status")
    var status: String = "-",
    @ColumnInfo(name = "Estate_date_begin")
    var date_begin: Long = 0,
    @ColumnInfo(name = "Estate_date_end")
    var date_end: Long = 0,
    @ColumnInfo(name = "Estate_personn")
    var personn: String = "-",
    @ColumnInfo(name = "Estate_location")
    var location: String = "-",
    @ColumnInfo(name = "Estate_nb_photo")
    var nb_photo: Int = 0,
    @ColumnInfo(name = "Estate_pharmacy")
    var prox_pharmacy: String = "Estate_pharmacy",
    @ColumnInfo(name = "Estate_park")
    var prox_park: String = "Estate_park",
    @ColumnInfo(name = "Estate_market")
    var prox_market: String = "Estate_market",
    @ColumnInfo(name = "Estate_school")
    var prox_school: String = "Estate_school",
    @Ignore
    var ImageUri: MutableList<String?>? = arrayListOf(),
    @Ignore
    var ImageDescription: MutableList<String?>? = arrayListOf()
){

    companion object {


        fun fromContentValues(values: ContentValues?): EstateR {
            val estate = EstateR()
            if (values != null) {
                if (values.containsKey("Estate_id")) estate.id = values.getAsInteger("Estate_id")
                if (values.containsKey("Estate_city")) estate.city =
                    values.getAsString("Estate_city")
                if (values.containsKey("Estate_adress")) estate.adress =
                    values.getAsString("Estate_adress")
                if (values.containsKey("Estate_type")) estate.type =
                    values.getAsString("Estate_type")
                if (values.containsKey("Estate_price")) estate.price =
                    values.getAsInteger("Estate_price")
                if (values.containsKey("Estate_surface")) estate.surface =
                    values.getAsInteger("Estate_surface")
                if (values.containsKey("Estate_number_of_room")) estate.number_of_room =
                    values.getAsInteger("Estate_number_of_room")
                if (values.containsKey("Estate_description")) estate.description =
                    values.getAsString("Estate_description")
                if (values.containsKey("Estate_pharmacy")) estate.prox_pharmacy =
                    values.getAsString("Estate_pharmacy")
                if (values.containsKey("Estate_park")) estate.prox_park =
                    values.getAsString("Estate_park")
                if (values.containsKey("Estate_market")) estate.prox_market =
                    values.getAsString("Estate_market")
                if (values.containsKey("Estate_school")) estate.prox_school =
                    values.getAsString("Estate_school")
                if (values.containsKey("Estate_status")) estate.status =
                    values.getAsString("Estate_status")
                if (values.containsKey("Estate_date_begin")) estate.date_begin =
                    values.getAsLong("Estate_date_begin")
                if (values.containsKey("Estate_date_end")) estate.date_end =
                    values.getAsLong("Estate_date_end")
                if (values.containsKey("Estate_personn")) estate.personn =
                    values.getAsString("Estate_personn")
                if (values.containsKey("Estate_location")) estate.location =
                    values.getAsString("Estate_location")
                if (values.containsKey("Estate_nb_photo")) estate.nb_photo =
                    values.getAsInteger("Estate_nb_photo")
            }
            return estate

        }
    }
}