package com.sofianem.realestatemanager

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.sofianem.realestatemanager.utils.Utils
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConnexionTest {

    @Test
    fun checkIfNetworkIsAvailable() {
        assertTrue(Utils.isInternetAvailable(InstrumentationRegistry.getInstrumentation().context)) }
}