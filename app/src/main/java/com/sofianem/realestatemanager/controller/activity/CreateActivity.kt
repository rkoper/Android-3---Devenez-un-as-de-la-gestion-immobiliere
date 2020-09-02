package com.sofianem.realestatemanager.controller.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.format.Time
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.appyvet.materialrangebar.RangeBar
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.sofianem.realestatemanager.R
import com.sofianem.realestatemanager.controller.adapter.CreateAdapter
import com.sofianem.realestatemanager.utils.GeocoderUtil
import com.sofianem.realestatemanager.utils.Utils
import com.sofianem.realestatemanager.viewmodel.MyViewModel
import kotlinx.android.synthetic.main.activity_create.*
import kotlinx.android.synthetic.main.dialog_custom_layout.view.*
import kotlinx.android.synthetic.main.dialog_description.view.*
import kotlinx.android.synthetic.main.dialog_layout.view.*
import kotlinx.android.synthetic.main.dialog_number_picker.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*


class CreateActivity : AppCompatActivity() {

    private lateinit var mMyViewModel: MyViewModel
    private var mUri: Uri? = null
    private val mListImagePath: MutableList<String?> = ArrayList()
    private val mListImageDescription: MutableList<String?> = ArrayList()
    lateinit var mView: View
    var mType: String = ""
    var mCity: String = ""
    private var mDescription: String = ""
    var mAddress: String = ""
    var mStatus: String = ""
    var mGeoLoc: String = ""
    private var mPerson: String = ""
    var mPrice: Int = 0
    var mSurface: Int = 0
    private var mNumberOfRoom: Int = 0
    private var mDateBegin: Long = 3
    private var mDateEnd: Long = 8888888888
    var mPom: Int = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        mMyViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)

      //  mMyViewModel.allWords.observe(this, androidx.lifecycle.Observer { t ->
        //    t.let { println("NEW INSERT" + "TEST ID ---------" + mPom) } })

        loadItem()
        OnClick()
        createData(mListImagePath, mListImageDescription)
    }


    private fun createData(listImage_path: MutableList<String?>, listimageDescription: MutableList<String?>) {
        activity_saveData_floating.setOnClickListener {
            mStatus = "ok"
            if (mAddress == "Adress" ||  mAddress == "-" ||  mAddress == "") {Toast.makeText(this, "Please add address...", Toast.LENGTH_SHORT).show()}
            else { Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show()
                mPom = mMyViewModel.storeData(
                    mType,
                    mCity,
                    mPrice,
                    mSurface,
                    mNumberOfRoom,
                    mDescription,
                    mAddress,
                    mGeoLoc,
                    mStatus,
                    mDateBegin,
                    mDateEnd,
                    mPerson,
                    listImage_path,
                    listimageDescription)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent) }
            mMyViewModel.allWords.observe(this, androidx.lifecycle.Observer { t ->
                t.let {
                    println("NEW INSERT " + " LOC + ID " + t.size + " " + mGeoLoc )
                    println("NEW INSERT " + " IMAGE + ID " + t.size + " " + listImage_path + "  " + listimageDescription )

                    mMyViewModel.getNearbyPlace1(t.size, mGeoLoc)


                } })
        }
   

    }

    private fun loadItem() {
        loadCity()
        loadRoom()
        loadPerson()
        loadDateBegin()
        loadDateEnd()
        loadPrice()
        loadAddress()
        loadType()
        loadSurface()
        loadDescription()
    }

    private fun loadDescription() {
        mDescription =  a_create_description.text.toString()
    }

    private fun loadType() {
        a_create_ed_type.setOnClickListener {
            val mBuilder = AlertDialog.Builder(this, R.style.MyDialogTheme)
            with(mBuilder) {
                setItems(listType) { dialog, i ->
                    a_create_ed_type.text = listType[i]
                    mType = a_create_ed_type.text.toString().trim()
                    dialog.dismiss() }
                val mDialog = mBuilder.create()
                mDialog.show() } } }

    private fun loadSurface() {
        a_create_rangebar_surface.setOnRangeBarChangeListener(object :
            RangeBar.OnRangeBarChangeListener { override fun onTouchEnded(rangeBar: RangeBar?) {}
            override fun onRangeChangeListener(rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String?, rightPinValue: String?) {
                a_create_tx_surface.text = "Surface : " + rightPinValue.toString() + "  Sq/ft"
                mSurface = rightPinValue!!.toInt() }
            override fun onTouchStarted(rangeBar: RangeBar?) {} }) }


    private fun loadAddress() {
        var streetNumber = ""
        var route = ""
        if (!Places.isInitialized()) { Places.initialize(applicationContext, "AIzaSyByK0jz-yxjpZFX88W8zjzTwtzMtkPYC4w") }

        val autocompleteFragment = Utils.configureAutoCompleteFrag(supportFragmentManager, resources, this, "Adress")
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                a_create_ed_adress.text = place.address
                println( " PLACE _ LAT LNG ------->" + place.latLng)
                place.addressComponents?.asList()?.forEach { Log.i("TAG", "AutoComplet: " + it.types + " " )
                    if (it.types.contains("street_number")) { streetNumber = it.name }
                    else if (it.types.contains("route")) { route = it.name }
                    else if (it.types.contains("locality")) {
                        a_create_ed_city.text = it.name
                        mCity = it.name } }

                a_create_ed_adress.text = "$streetNumber $route"
                a_create_ed_adress.visibility = View.VISIBLE
                mAddress = "$streetNumber $route"

                mGeoLoc = GeocoderUtil.getlocationForListv2( mAddress, mCity, this@CreateActivity)

                println( " mGeoLoc ------->" + mGeoLoc)


            }
            override fun onError(status: Status) { Log.i("TAG", "An error occurred: $mStatus") } })
    }

    private fun loadPrice() {
        a_create_rangebar_price.setOnRangeBarChangeListener(object :
            RangeBar.OnRangeBarChangeListener { override fun onTouchEnded(rangeBar: RangeBar?) {}
            override fun onRangeChangeListener(rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String?, rightPinValue: String?) {
                val value = rightPinValue.toString() + "0000"
                val displayValue = Utils.addWhiteSpace(value)
                a_create_tx_pric.text = "Price : " + displayValue + " $"
                mPrice = value.toInt() }
            override fun onTouchStarted(rangeBar: RangeBar?) {} })
    }

    private fun loadDateEnd() {
        a_create_ed_dateend.setOnClickListener {
            val dpd =
                DatePickerDialog.OnDateSetListener { _, y, m, d ->
                    a_create_ed_dateend.text =  Utils.formatDate(y,m,d)
                    mDateEnd = Utils.convertToEpoch(Utils.formatDate(y,m,d)) }
            val now = Time()
            now.setToNow()
            val d = DatePickerDialog(this, R.style.MyAppThemeCalendar, dpd, now.year, now.month, now.monthDay)
            d.show()
            d.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(resources.getColor(R.color.colorD))
            d.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(resources.getColor(R.color.colorD)) } }

    private fun loadDateBegin() {
        a_create_ed_datebegin.setOnClickListener {
            val dpd =
                DatePickerDialog.OnDateSetListener { _, y, m, d ->
                    a_create_ed_datebegin.text =  Utils.formatDate(y,m,d)
                    mDateBegin =  Utils.convertToEpoch(Utils.formatDate(y,m,d)) }
            val now = Time()
            now.setToNow()
            val d = DatePickerDialog(this, R.style.MyAppThemeCalendar, dpd, now.year, now.month, now.monthDay)
            d.show()
            d.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(resources.getColor(R.color.colorD))
            d.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(resources.getColor(R.color.colorD)) } }

    private fun loadPerson() {
        a_create_ed_personn.setOnClickListener {
            val view = this.currentFocus
            view?.let { v ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(v.windowToken, 0) }
            val mBuilder = AlertDialog.Builder(this, R.style.MyDialogTheme)
            with(mBuilder) {
                setItems(listPerson) { dialog, i ->
                    a_create_ed_personn.text = listPerson[i]
                    mPerson = a_create_ed_personn.text.toString().trim()
                    dialog.dismiss() }
                val mDialog = mBuilder.create()
                mDialog.show() } } }

    private fun loadRoom() {
        a_create_ed_room.setOnClickListener {
            val d = Dialog(this)
            d.setContentView(R.layout.dialog_number_picker)
            d.numberPicker.maxValue = 15
            d.numberPicker.minValue = 1
            d.numberPicker.wrapSelectorWheel = false
            d.button_check_numberpicker.setOnClickListener {
                a_create_ed_room.text = d.numberPicker.value.toString()
                mNumberOfRoom = d.numberPicker.value
                d.dismiss() }
            d.show() } }

    private fun loadCity() { mCity = a_create_ed_city.text.toString().trim() }

    private fun OnClick() { photoUpload.setOnClickListener { selectImage() }}

    private fun selectImage() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(mDialogView)
        val mAlertDialog = builder.show()
        mDialogView.dialog_gallery.setOnClickListener { mAlertDialog.dismiss()
            if (Utils.CheckExternalStorage(this) != PackageManager.PERMISSION_GRANTED) { Utils.askForStorage(this) }
            else { openGallery() } }

        mDialogView.dialog_photo.setOnClickListener { mAlertDialog.dismiss()
            if ( Utils.CheckCamera(this) != PackageManager.PERMISSION_GRANTED) { Utils.askForCamera(this) }
            else { capturePhoto() } }

        mDialogView.dialog_cancel.setOnClickListener { mAlertDialog.run { dismiss() }
            Toast.makeText(applicationContext, "cancelled", Toast.LENGTH_SHORT).show() } }


    private fun capturePhoto() {
        val capturedImage = File(externalCacheDir, FILE_NAME)
        if (capturedImage.exists()) { capturedImage.delete() }
        capturedImage.createNewFile()
        mUri = if (Build.VERSION.SDK_INT >= 24) { FileProvider.getUriForFile(this, PATH_PROVIDER, capturedImage) }
        else { Uri.fromFile(capturedImage) }
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
        startActivityForResult(intent, OPERATION_CAPTURE_PHOTO) }

    private fun openGallery() {
        val intent = Intent(GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, OPERATION_CHOOSE_PHOTO) }

    private fun renderImage(imagePath: String?) {
        if (imagePath != null) { val bitmap = BitmapFactory.decodeFile(imagePath) }
        else { Toast.makeText(this, "ImagePath == null", Toast.LENGTH_SHORT).show() } }

    private fun getImagePath(uri: Uri?, selection: String?): String {
        var path: String? = null
        val cursor = contentResolver.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) { path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)) }
            cursor.close() }
        return path!! }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantedResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantedResults)
        when (requestCode) {
            1 -> if (grantedResults.isNotEmpty() && grantedResults[0] == PackageManager.PERMISSION_GRANTED) { openGallery() }
            else { Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show() }

            2 -> if (grantedResults.isNotEmpty() && grantedResults[0] == PackageManager.PERMISSION_GRANTED) { capturePhoto() }
            else { Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show() } } }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            OPERATION_CAPTURE_PHOTO ->
                if (resultCode == Activity.RESULT_OK)
                { val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(mUri))
                    saveImage(bitmap) }

            OPERATION_CHOOSE_PHOTO ->
                if (resultCode == Activity.RESULT_OK)
                { var imagePath: String? = null
                    val uri = data!!.data
                    if (DocumentsContract.isDocumentUri(this, uri)) {
                        val docId = DocumentsContract.getDocumentId(uri)
                        if (MEDIA_PROVIDER == uri.authority) { imagePath = Utils.createPathForMedia(docId, this) }
                        else if (DOWNLOAD_PROVIDER == uri.authority) { imagePath = Utils.createPathForDownload(docId,this) } }

                    else if ("content".equals(uri.scheme, ignoreCase = true)) { imagePath = getImagePath(uri, null) }
                    else if ("file".equals(uri.scheme, ignoreCase = true)) { imagePath = uri.path }

                    renderImage(imagePath)
                    createAlertDialog(imagePath) } } }

    private fun createAlertDialog(imagePath: String?) {
        mListImagePath.add(imagePath)
        val mDialogViewForImageInfo = LayoutInflater.from(this).inflate(R.layout.dialog_custom_layout, null)
        var builderForImageInfo = AlertDialog.Builder(this)
        builderForImageInfo.setView(mDialogViewForImageInfo)
        val mAlertDialogForImageInfo = builderForImageInfo.show()

        mDialogViewForImageInfo.custom_dialog_ok.setOnClickListener {
            mAlertDialogForImageInfo.dismiss()
            val photoInfo: String? = mDialogViewForImageInfo.custom_dialog_txt.text.toString()
            if (photoInfo.isNullOrBlank() || imagePath.isNullOrBlank()) { println(" Error") }
            else { mListImageDescription.add(photoInfo)
                createRV(mListImagePath, mListImageDescription) } } }

    private fun createRV(listimagePath: MutableList<String?>, listimageDescription: MutableList<String?>) {
        create_recyclerview.layoutManager = GridLayoutManager(applicationContext, 6)
        create_recyclerview.adapter =  CreateAdapter(listimagePath, listimageDescription, this)
        createData(mListImagePath, listimageDescription) }

    private fun saveImage(myBitmap: Bitmap) {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)
        val wallpaperDirectory = File((Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        if (!wallpaperDirectory.exists()) { wallpaperDirectory.mkdirs() }
        try { val f = Utils.createPathAndSave(wallpaperDirectory,bytes, this )
            createAlertDialog(f.absolutePath) } catch (e1: IOException) { e1.printStackTrace() } }

    companion object {
        const val IMAGE_DIRECTORY = "/so"
        const val FILE_NAME = "My_Captured_Photo.jpg"
        const val PATH_PROVIDER   = "com.SofianeM.RealEstateManager.fileprovider"
        const val GET_CONTENT  = "android.intent.action.GET_CONTENT"
        const val MEDIA_PROVIDER  =   "com.android.providers.media.documents"
        const val DOWNLOAD_PROVIDER  =  "com.android.providers.downloads.documents"
        val listPerson = arrayOf("Leonardo", "Michel-Ange", "Raphael", "Donatello")
        val listType = arrayOf("Apartment", "House", "Loft", "Castle")
        const val OPERATION_CAPTURE_PHOTO = 1
        const val OPERATION_CHOOSE_PHOTO = 2
    }
}
