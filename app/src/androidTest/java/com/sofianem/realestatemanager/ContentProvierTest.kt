package com.sofianem.realestatemanager

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.sofianem.realestatemanager.data.DataBase.EstateDatabase
import com.sofianem.realestatemanager.providers.ItemContentProvider
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ContentProvierTest {

    private lateinit var mContentResolver:ContentResolver

    @Before
    fun setUp(){
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context,EstateDatabase::class.java).allowMainThreadQueries().build()
        mContentResolver = InstrumentationRegistry.getInstrumentation().targetContext.contentResolver }

    @Test
    fun emptyCase(){
        val cursor = mContentResolver.query(ContentUris.withAppendedId(ItemContentProvider().URI_ESTATE, 99),null,null,null,null)
       assertNotNull(cursor); assertEquals(0, cursor.count); cursor.close() }


    @Test
    fun newItem() { val rnds = (0..999999999).random() ; val values = ContentValues()
        values.put("Estate_id", rnds); values.put("Estate_city", "Paris"); values.put("Estate_adress", "7 rue ordener")
        values.put("Estate_type", "Loft");  values.put("Estate_price", 2000000); values.put("Estate_surface", 55)
        values.put("Estate_number_of_room", 2); values.put("Estate_description", "Sympa");  values.put("Estate_pharmacy", "ok")
        values.put("Estate_park", "ok"); values.put("Estate_market", "ok");  values.put("Estate_school", "ok")
        values.put("Estate_status", "ok"); values.put("Estate_date_begin", 1);  values.put("Estate_date_end", 3047298191)
        values.put("Estate_personn", "Roger"); values.put("Estate_location", "48.890184, 2.358677");  values.put("Estate_nb_photo", 2)
        mContentResolver.insert(ItemContentProvider().URI_ESTATE, values)

        val cursor = mContentResolver.query(ContentUris.withAppendedId(
                ItemContentProvider().URI_ESTATE, rnds.toLong()), null, null, null, null)
        assertNotNull(cursor)
        assertEquals(1, cursor.count)
        assertEquals(true, cursor.moveToFirst())
        assertEquals("Loft", cursor.getString(cursor.getColumnIndexOrThrow("Estate_type")))
        assertEquals("ok", cursor.getString(cursor.getColumnIndexOrThrow("Estate_school"))) }
}