package com.sofianem.realestatemanager

import com.sofianem.realestatemanager.utils.Utils
import org.junit.Assert.assertEquals
import org.junit.Test

class  ConvertEuroTest {
    @Test
    fun dollar_isCorrect() {
        val a =  Utils.convertDollarToEuro(15)
        assertEquals(13, a ) }
}