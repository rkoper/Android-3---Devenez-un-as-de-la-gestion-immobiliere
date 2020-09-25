package com.sofianem.realestatemanager

import com.sofianem.realestatemanager.utils.Utils
import org.junit.Assert
import org.junit.Test

class ConverteOpenClassDateTest {

    @Test
    fun date_isCorrect() {
        val a =  Utils.getTodayDate( )
        Assert.assertEquals("25/09/2020", a)
    }
}