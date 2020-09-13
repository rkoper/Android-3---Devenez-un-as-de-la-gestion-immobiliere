package com.sofianem.realestatemanager.providers

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.sofianem.realestatemanager.data.dataBase.AllDatabase
import com.sofianem.realestatemanager.data.model.EstateR

class ItemContentProvider : ContentProvider() {

    // FOR DATA
    val AUTHORITY = "com.sofianem.realestatemanager.providers"
    val TABLE_NAME = EstateR::class.java.simpleName
    var URI_ESTATE = Uri.parse("content://$AUTHORITY/$TABLE_NAME")

    override fun insert(mUri: Uri?, mCValues: ContentValues?): Uri {
        if (context != null && mCValues != null){
            Log.e("EstateContentProvider","ContentValues : $mCValues")
            val index = AllDatabase.getInstance(context)?.estateDao()?.insertItem(EstateR.fromContentValues(mCValues))
            if (index != 0L){
                context.contentResolver.notifyChange(mUri,null)
                return ContentUris.withAppendedId(mUri, index!!)
            }
        }

        throw IllegalArgumentException("Error insert")
    }

    override fun query(mUri: Uri?, mArrayOne: Array<out String>?, mArrayTwo: String?, mArrayThree: Array<out String>?, mArrayFour: String?): Cursor? {
        if (context != null){
            val index  = ContentUris.parseId(mUri).toInt()
            val cursor = AllDatabase.getInstance(context)?.estateDao()?.getItemsWithCursor(index)
            cursor?.setNotificationUri(context.contentResolver,mUri)
            return cursor
        }

        throw IllegalArgumentException("Error query")
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun update(mUri: Uri?, mCValues: ContentValues?, mString: String?, mArray: Array<out String>?): Int {
        if (context != null && mCValues != null){
            val count:Int = AllDatabase.getInstance(context)?.estateDao()!!.updateItem(EstateR.fromContentValues(mCValues))
            context.contentResolver.notifyChange(mUri,null)
            return count
        }

        throw IllegalArgumentException("Error update")
    }

    override fun delete(mUri: Uri?, mString: String?, mArray: Array<out String>?): Int {
        throw IllegalArgumentException("Not allowed")
    }

    override fun getType(mUri: Uri?): String {
        return "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
    }
}