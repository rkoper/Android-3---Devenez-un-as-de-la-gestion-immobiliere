package com.sofianem.realestatemanager.utils

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Typeface
import android.media.ExifInterface
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.MediaStore
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.sofianem.realestatemanager.R
import com.sofianem.realestatemanager.data.model.EstateR
import kotlinx.android.synthetic.main.activity_create.*
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToLong

/**
 * Created by Philippe on 21/02/2018.
 */
object Utils {

//    private val mContext = MyApplication.applicationContext()


    fun convertDollarToEuro(dollars: Int): Int {
        return (dollars * 0.85).roundToLong().toInt()
    }

    fun convertEuroToDollar(euros: Int): Int {
        return (euros * 1.18).roundToLong().toInt()
    }

    fun convertSqTom2(sq: Int): Int {
        return (sq * 0.092).roundToLong().toInt()
    }

    val todayDate: String
        get() {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd")
            return dateFormat.format(Date())
        }

    fun formatDateV2(date: Date): Long{
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = sdf.format(date)
        return  SimpleDateFormat("dd/MM/yyyy").parse(currentDate).time
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun formatLatLng(latlngString: String): LatLng {
        val mL = latlngString.split(",".toRegex()).toTypedArray()
        val latitude: Double = mL[0].toDouble()
        val longitude: Double = mL[1].toDouble()
        return LatLng(latitude, longitude)
    }

    fun latitude(latlngString: String): Double {
        val mL = latlngString.split(",".toRegex()).toTypedArray()
        return mL[0].toDouble()
    }

    fun longitude(latlngString: String): Double {
        val mL = latlngString.split(",".toRegex()).toTypedArray()
        return mL[1].toDouble()
    }

    fun currentLat(currentLocation: String): Double {
        val loc = currentLocation.split(",")
         return loc[0].toDouble()
    }

    fun currentLng(currentLocation: String): Double {
        val loc = currentLocation.split(",")
         return loc[1].toDouble()
    }

    fun calculateDistance(
        x1: Double,
        y1: Double,
        x2: Double,
        y2: Double
    ): Double {
        val xSquare = (x1 - x2).pow(2.0)
        val ySquare = (y1 - y2).pow(2.0)
        return Math.sqrt(xSquare + ySquare) * 100000
    }

    fun formatDate(y:Int, m:Int, d:Int) : String {
        val s = m + 1
        if  (s < 10 && d >10 ) {return "$d/0$s/$y"}
        if  (d <10 && s > 10 ) {return  "0$d/$s/$y"}
        if (s < 10 && d <10 ) {return  "0$d/0$s/$y"}
        else {return  "$d/$s/$y"}

    }

    fun convertToEpoch(date:String) : Long {
        return  SimpleDateFormat("dd/MM/yyyy").parse(date).time
    }

    fun convertToLocalB(lstE : EstateR) : String {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val displayDateB = sdf.format(Date(lstE.date_begin))
        return displayDateB
    }
    fun convertToLocalE(lstE : EstateR) : String {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val displayDateE = sdf.format(Date(lstE.date_end))
        return displayDateE
    }

    fun CheckExternalStorage(mContext: Context) : Int{
        return ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) }

    fun CheckCamera(mContext: Context) : Int{
        return ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CAMERA) }

    fun askForStorage(mActivity: Activity){
        ActivityCompat.requestPermissions(mActivity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
    }
    fun askForCamera(mActivity: Activity){
        ActivityCompat.requestPermissions(mActivity, arrayOf(android.Manifest.permission.CAMERA), 2)
    }

    fun askForLoc(mContext: Context): Int {
       return ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) }



    private fun getImagePath(uri: Uri?, selection: String?, mContext: Context): String {
        var path: String? = null
        val cursor = mContext.contentResolver.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path!!
    }

    fun configureAutoCompleteFrag(
        supportFragmentManager: FragmentManager,
        resources:Resources,
        mContext:Context,
    hint:String) : AutocompleteSupportFragment{
        var autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
        var fView: View? = autocompleteFragment?.view
        var etTextInput: EditText? = fView?.findViewById(R.id.places_autocomplete_search_input)
        etTextInput?.setBackgroundColor(resources.getColor(R.color.colorB))
        etTextInput?.setTextColor(resources.getColor(R.color.colorD))
        etTextInput?.setHintTextColor(resources.getColor(R.color.colorD))
        etTextInput?.gravity = Gravity.CENTER
        etTextInput?.hint = hint
        val font: Typeface? = ResourcesCompat.getFont(mContext, R.font.montserrat)
        etTextInput?.setTypeface(font, Typeface.BOLD)
        etTextInput?.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.textadaptsize))
        val searchIcon = (autocompleteFragment?.view as LinearLayout).getChildAt(0) as ImageView
        searchIcon.visibility = View.GONE
        autocompleteFragment?.setTypeFilter(TypeFilter.ADDRESS)
        autocompleteFragment?.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS_COMPONENTS))


                return autocompleteFragment
    }

     fun addWhiteSpace(x: String) : String{
        val n = 3
        val str = StringBuilder(x)
        var idx = str.length - n
        while (idx > 0) {
            str.insert(idx, " ")
            idx = idx - n
        }
        return str.toString()
    }

    fun createPathForMedia(docId: String, mContext: Context) : String{
        val id = docId.split(":")[1]
        val mMedia = MediaStore.Images.Media._ID + "=" + id
        return  getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mMedia, mContext)
    }

    fun createPathForDownload(docId: String, mContext: Context) : String{
        val contentUri = ContentUris.withAppendedId(
            Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(docId))
        return  getImagePath(contentUri, null, mContext)
    }

    fun createPathAndSave(wallpaperDirectory: File, bytes:ByteArrayOutputStream, mContext: Context) : File{
        val f = File(wallpaperDirectory, ((Calendar.getInstance().timeInMillis).toString() + ".png"))
        f.createNewFile()
        val fo = FileOutputStream(f)
        fo.write(bytes.toByteArray())
        MediaScannerConnection.scanFile(mContext, arrayOf(f.path), arrayOf("image/png"), null)
        fo.close()
        return  f
    }

        fun calculateLoanAmount(rate:Float, amount:Int, term:Int): String{
            var t1 = rate/100
            val t2 = t1/12
            val t3 = amount*t2
            val y1 = 1.plus(t2)
            val y2 = y1.pow(-term)
            val y3 = 1-y2
            val result = t3/y3

           return "%.0f".format(result)
        }


    fun rotateImage(path: String?): Bitmap? {
        val bitmap: Bitmap = BitmapFactory.decodeFile(path)
        var rotate = 0
        val exif = ExifInterface(path)
        val orientation: Int = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
            ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
            ExifInterface.ORIENTATION_NORMAL -> rotate = 90
            0  -> rotate = 0
        }
        val matrix = Matrix()
        matrix.postRotate(rotate.toFloat())
        return Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width,
            bitmap.height, matrix, true
        )
    }

    fun <T : Any> handleApiError(resp: Response<T>): AppResult.Error {
        val error = ApiErrorUtils.parseError(resp)
        return AppResult.Error(Exception(error.message))
    }

    fun <T : Any> handleSuccess(response: Response<T>): AppResult<T> {
        response.body()?.let {
            return AppResult.Success(it)
        } ?: return handleApiError(response)
    }
}