package com.sofianem.realestatemanager

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sofianem.realestatemanager.utils.GeocoderUtil
import com.sofianem.realestatemanager.utils.MyApplication
import com.sofianem.realestatemanager.utils.Utils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LatLngTest {
    val mContext = MyApplication.applicationContext()
    @Test
    fun LocationTest() {
        val a =  Utils.getlocationForList("7 Rue Ordener" , "Paris" , mContext)
        Assert.assertEquals("48.890184,2.3586774", a)
    }
}