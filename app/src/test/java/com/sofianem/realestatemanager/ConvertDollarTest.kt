package com.sofianem.realestatemanager

import com.sofianem.realestatemanager.utils.Utils
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class  ConvertDollarTest {
    @Test
    fun convertDollarTest() {
        val a =  Utils.convertEuroToDollar(15)
        assertEquals(18 , a )
    }
}