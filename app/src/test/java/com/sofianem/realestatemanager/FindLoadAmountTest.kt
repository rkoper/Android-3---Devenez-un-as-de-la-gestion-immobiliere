package com.sofianem.realestatemanager

import com.sofianem.realestatemanager.utils.Utils
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class  FindLoadAmountTest {
    @Test
    fun addition_isCorrect() {
        val resultDisplay =  Utils.calculateLoanAmount(1.1f, 100000, 215)
        assertEquals("513", resultDisplay)
    }
}