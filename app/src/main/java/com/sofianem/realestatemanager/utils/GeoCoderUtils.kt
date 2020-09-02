package com.sofianem.realestatemanager.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.widget.Toast
import com.sofianem.realestatemanager.data.model.EstateR
import java.io.IOException

/**
 * Created by soulduse on 2018. 2. 10..
 */
object GeocoderUtil {

    fun getlocation(id: Int, it: List<EstateR>): String {
        val context = MyApplication.applicationContext()
        var geocodeMatches: List<Address>? = null
        val id: Int = id
        try { val mId: Int = id; geocodeMatches = Geocoder(context).getFromLocationName(it[id].adress + ", " + it[id].city, 1) }
        catch (ioException: IOException) { Toast.makeText(context, "error w/ geocoder", Toast.LENGTH_SHORT).show() }
        val geoLat: Double? = geocodeMatches?.get(0)?.latitude
        val geoLng: Double? = geocodeMatches?.get(0)?.longitude
        val geoL: String = (geoLat.toString() + "," + geoLng.toString())

        return geoL
    }

    fun getlocationForList(id: Int?, adress: String?, city: String?, mContext: Context): String {
        var errorMessage = ""
        var geocodeMatches: List<Address>? = null
        val id: Int? = id

        try { val mId: Int? = id
            geocodeMatches = Geocoder(mContext).getFromLocationName(
                "$adress, $city", 1) } catch (ioException: IOException) { }

        val geoLat: Double? = geocodeMatches?.get(0)?.latitude ; val geoLng: Double? = geocodeMatches?.get(0)?.longitude
        val geoL: String = (geoLat.toString() + "," + geoLng.toString())
        return geoL }

    fun lat(geoL:String) : String  { val lat  = geoL.split(",".toRegex()).toTypedArray()
        return lat[0] }

    fun lng(geoL:String) : String  { val lng = geoL.split(",".toRegex()).toTypedArray()
        return lng[1] }


    fun getlocationForListv2(adress: String?, city: String?, mContext: Context): String {
        var errorMessage = ""
        var geocodeMatches: List<Address>? = null

        try {
            geocodeMatches = Geocoder(mContext).getFromLocationName(
                "$adress, $city", 1) } catch (ioException: IOException) { }

        val geoLat: Double? = geocodeMatches?.get(0)?.latitude ; val geoLng: Double? = geocodeMatches?.get(0)?.longitude
        val geoL: String = (geoLat.toString() + "," + geoLng.toString())
        return geoL }

    fun latv2(geoL:String) : String  { val lat  = geoL.split(",".toRegex()).toTypedArray()
        return lat[0] }

    fun lngv2(geoL:String) : String  { val lng = geoL.split(",".toRegex()).toTypedArray()
        return lng[1] }
}
