package com.sofianem.realestatemanager

import com.sofianem.realestatemanager.utils.Utils
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class  ConvertEpochTest {
    @Test
    fun epoch_isCorrect() {
        val a =  Utils.convertToEpoch("21/05/1984")
        assertEquals(453938400000, a )
    }
}