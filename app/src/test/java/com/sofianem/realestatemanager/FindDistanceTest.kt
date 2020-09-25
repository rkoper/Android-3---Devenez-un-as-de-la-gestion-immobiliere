package com.sofianem.realestatemanager

import com.sofianem.realestatemanager.utils.Utils
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.roundToInt

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class  FindDistanceTest {
    @Test
    fun addition_isCorrect() {
        val a = Utils.calculateDistance(48.890100, 2.358660, 48.89156, 2.34978).roundToInt().toString() +  " Sq"
        assertEquals("990 Sq" , a )
    }
    @Test
    fun addition_isCorrectv2() {
        val a = Utils.calculateDistance(48.89630649999999,2.3432004, 48.8914799,2.3397897999999997).roundToInt().toString() +  " Sq"
        assertEquals("650 Sq" , a )
    }
}