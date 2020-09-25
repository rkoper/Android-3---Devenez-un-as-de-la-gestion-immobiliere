package com.sofianem.realestatemanager

import com.sofianem.realestatemanager.utils.Utils
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class  ConvertDateTest {
    @Test
    fun addition_isCorrect() {
        val a =  Utils.formatDate(1984, 4 ,21 )
        assertEquals("21/05/1984", a )
    }
}