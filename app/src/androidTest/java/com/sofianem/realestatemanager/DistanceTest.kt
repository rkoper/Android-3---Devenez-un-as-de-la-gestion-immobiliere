package com.sofianem.realestatemanager

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.sofianem.realestatemanager.utils.Utils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.roundToInt

@RunWith(AndroidJUnit4::class)
class DistanceTest {
    @Test
    fun locationTest() {
        val locA =  Utils.getlocationForList( "7 Rue Ordener" , "Paris" , InstrumentationRegistry.getInstrumentation().context )
        val locB =  Utils.getlocationForList( "19 Rue Ternaux" , "Paris" , InstrumentationRegistry.getInstrumentation().context)

        val locA1 = locA.split(",")
        val locA2 = locB.split(",")
        

        val distance = Utils.calculateDistance(
            locA1[0].toDouble(), locA1[1].toDouble(),
            locA2[0].toDouble(), locA2[1].toDouble()).roundToInt().toString() +  " Sq"





        Assert.assertEquals("3254 Sq", distance)
    }
}