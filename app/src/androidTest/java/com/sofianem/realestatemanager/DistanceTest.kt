package com.sofianem.realestatemanager

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sofianem.realestatemanager.utils.GeocoderUtil
import com.sofianem.realestatemanager.utils.MyApplication
import com.sofianem.realestatemanager.utils.Utils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.roundToInt

@RunWith(AndroidJUnit4::class)
class DistanceTest {
    val mContext = MyApplication.applicationContext()
    @Test
    fun LocationTest() {
        val locA =  Utils.getlocationForList( "7 Rue Ordener" , "Paris" , mContext)
        val locB =  Utils.getlocationForList( "19 Rue Ternaux" , "Paris" , mContext)

        val locA1 = locA.split(",")
        val locA2 = locB.split(",")
        

        val distance = Utils.calculateDistance(
            locA1[0].toDouble(), locA1[1].toDouble(),
            locA2[0].toDouble(), locA2[1].toDouble()).roundToInt().toString() +  " m"





        Assert.assertEquals("2958 m", distance)
    }
}