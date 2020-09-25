package com.sofianem.realestatemanager

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.sofianem.realestatemanager.utils.MyApplication
import com.sofianem.realestatemanager.utils.Utils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LatLngTest {
    @Test
    fun locationTest() {
        val a =  Utils.getlocationForList("7 Rue Ordener" , "Paris" , InstrumentationRegistry.getInstrumentation().context)
        Assert.assertEquals("48.890184,2.3586774", a)
    }
}