package com.sofianem.realestatemanager.controller.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.format.Time
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.appyvet.materialrangebar.RangeBar
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.sofianem.realestatemanager.R
import com.sofianem.realestatemanager.controller.adapter.CreateAdapter
import com.sofianem.realestatemanager.controller.view.GridItemDecoration
import com.sofianem.realestatemanager.utils.Utils
import com.sofianem.realestatemanager.viewmodel.MyViewModel
import com.sofianem.realestatemanager.viewmodel.MyViewModelForImages
import com.sofianem.realestatemanager.viewmodel.MyViewModelForPlaces
import kotlinx.android.synthetic.main.activity_create.*
import kotlinx.android.synthetic.main.dialog_custom_layout.*
import kotlinx.android.synthetic.main.dialog_custom_layout.view.*
import kotlinx.android.synthetic.main.dialog_layout.*
import kotlinx.android.synthetic.main.dialog_layout.view.*
import kotlinx.android.synthetic.main.dialog_number_picker.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt


class CreateActivity : AppCompatActivity() {
    private var mUri: Uri? = null
    private val mListImagePath: MutableList<String?> = ArrayList()
    private val mListImageDescription: MutableList<String?> = ArrayList()
    private var mType: String = ""
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
    private var mCreateId: Int = 99
    private var mNbPhoto:Int = 0
    private var mNewID : Long = 66666
    private val mMyViewModel by viewModel<MyViewModel>()
    private val mMyViewModelForImages by viewModel<MyViewModelForImages>()
    private val mMyViewModelForPlaces by viewModel<MyViewModelForPlaces>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        mMyViewModel.mAllEstate.observe(this, androidx.lifecycle.Observer {
            mCreateId = if (it.isEmpty()) {
                1
            } else {
                it.last().id.plus(1)
            }
        })

        loadItem()
        onClickHouse()
        onClickAddPhoto()
        createData(mListImagePath, mListImageDescription)
    }

    private fun onClickHouse() {
        activity_home_floating_create.setOnClickListener {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)}
    }


    private fun createData(listImage_path: MutableList<String?>, listimageDescription: MutableList<String?>) {

        activity_saveData_floating.setOnClickListener {
            mDescription = a_create_description.text.toString()
            mStatus = "ok"
            mNbPhoto = listimageDescription.size
            if (mAddress == "Adress" ||  mAddress == "-" ||  mAddress == "") {Toast.makeText(this, "Please add address...", Toast.LENGTH_SHORT).show()}
            else { Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show()
                println(" ----- create 1 -------->>>>>>")
                mNewID =
                    mMyViewModel.insertTodo(
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
                    mNbPhoto,
                    listImage_path,
                    listimageDescription)




                mListTypeOfLocation.forEach { typeLocation ->  mMyViewModelForPlaces.saveLocation(mGeoLoc, typeLocation, mCreateId) }

                mMyViewModelForImages.storeImageData(mCreateId, listImage_path, listimageDescription)



                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(NEW_ID, mNewID.toInt())
                startActivity(intent) } }


            }





    private fun loadItem() {
        loadCity()
        loadRoom()
        loadPerson()
        loadDateBegin()
        loadPrice()
        loadAddress()
        loadType()
        loadSurface()
    }


    private fun loadType() {
        a_create_ed_type.setOnClickListener {
            val mBuilder = AlertDialog.Builder(this, R.style.MyDialogTheme)
            mBuilder.setItems(listType) { dialog, i ->
                a_create_ed_type.text = listType[i]
                mType = a_create_ed_type.text.toString().trim()
                dialog.dismiss() }
            val mDialog = mBuilder.create()
            mDialog.show()
        } }

    private fun loadSurface() {
        a_create_rangebar_surface.setOnRangeBarChangeListener(object :
            RangeBar.OnRangeBarChangeListener { override fun onTouchEnded(rangeBar: RangeBar?) {}
            override fun onRangeChangeListener(rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String?, rightPinValue: String?) {
                val mDisplaySurface = "Surface : " + rightPinValue.toString() + "  Sq/ft"
                a_create_tx_surface.text = mDisplaySurface
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
                place.addressComponents?.asList()?.forEach { Log.i("TAG", "AutoComplet: " + it.types + " " )
                    when {
                        it.types.contains("street_number") -> { streetNumber = it.name }
                        it.types.contains("route") -> { route = it.name }
                        it.types.contains("locality") -> {
                            a_create_ed_city.text = it.name
                            mCity = it.name }
                    }
                }
                val mDisplaceAdress = "$streetNumber $route"
                a_create_ed_adress.text = mDisplaceAdress
                a_create_ed_adress.visibility = View.VISIBLE
                mAddress = "$streetNumber $route"

               mGeoLoc = Utils.getlocationForList( mAddress, mCity, this@CreateActivity)
            }
            override fun onError(status: Status) { Log.i("TAG", "An error occurred: $mStatus")
            } })
    }

    private fun loadPrice() {
        a_create_rangebar_price.setOnRangeBarChangeListener(object :
            RangeBar.OnRangeBarChangeListener { override fun onTouchEnded(rangeBar: RangeBar?) {}
            override fun onRangeChangeListener(rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String?, rightPinValue: String?) {
                val value = rightPinValue.toString() + "0000"
                val mDisplayPrice = "Price : " + Utils.addWhiteSpace(value) + " $"
                a_create_tx_pric.text = mDisplayPrice
                mPrice = value.toInt() }
            override fun onTouchStarted(rangeBar: RangeBar?) {} })
    }

    private fun loadDateBegin() {
        mDateBegin = Timestamp(System.currentTimeMillis()).time
        a_create_ed_datebegin.text = SimpleDateFormat("dd/MM/yyyy").format(Date())
            a_create_ed_datebegin.setOnClickListener {
            val dpd = DatePickerDialog.OnDateSetListener { _, y, m, d ->
                    a_create_ed_datebegin.text =  Utils.formatDate(y,m,d)
                    mDateBegin =  Utils.convertToEpoch(Utils.formatDate(y,m,d))             }
            val now = Time()
            now.setToNow()
            val d = DatePickerDialog(this, R.style.MyAppThemeCalendar, dpd, now.year, now.month, now.monthDay)
            d.show()
            d.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.colorB))
            d.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.colorB)) } }

    private fun loadPerson() {
        a_create_ed_personn.setOnClickListener {
            val view = this.currentFocus
            view?.let { v ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(v.windowToken, 0) }
            val mBuilder = AlertDialog.Builder(this, R.style.MyDialogTheme)
            mBuilder.setItems(listPerson) { dialog, i ->
                a_create_ed_personn.text = listPerson[i]
                mPerson = a_create_ed_personn.text.toString().trim()
                dialog.dismiss() }
            val mDialog = mBuilder.create()
            mDialog.show()
        } }

    private fun loadRoom() {
        a_create_ed_room.setOnClickListener {
            val d = Dialog(this)
            d.requestWindowFeature(Window.FEATURE_NO_TITLE)
            d.window.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
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

    private fun onClickAddPhoto() { photoUpload.setOnClickListener { selectImage() }}

    private fun selectImage() {
        val d = Dialog(this)
        d.requestWindowFeature(Window.FEATURE_NO_TITLE)
        d.window.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
        d.setContentView(R.layout.dialog_layout)

        d.dialog_gallery.setOnClickListener { d.dismiss()
            if (Utils.checkExternalStorage(this) != PackageManager.PERMISSION_GRANTED) { Utils.askForStorage(this) }
            else { openGallery() } }

        d.dialog_photo.setOnClickListener { d.dismiss()
            if ( Utils.checkCamera(this) != PackageManager.PERMISSION_GRANTED) { Utils.askForCamera(this) }
            else { capturePhoto() } }

        d.dialog_cancel.setOnClickListener { d.run { dismiss() }
            Toast.makeText(applicationContext, "cancelled", Toast.LENGTH_SHORT).show() }
        d.show()}


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
        val d = Dialog(this)
        d.requestWindowFeature(Window.FEATURE_NO_TITLE)
        d.window.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
        d.setContentView(R.layout.dialog_custom_layout)
        d.show()

        d.custom_dialog_txt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (start == 0 )
                { d.custom_dialog_ok.isVisible = false
                    d.custom_dialog_not_ok.isVisible = true}
                else {
                    d.custom_dialog_ok.isVisible = true
                    d.custom_dialog_not_ok.isVisible = false}
            } })

        d.custom_dialog_not_ok.setOnClickListener {
            Toast.makeText(this, " Please add description...", Toast.LENGTH_SHORT).show() }

        d.custom_dialog_ok.setOnClickListener {
            d.dismiss()
            val photoInfo: String? = d.custom_dialog_txt.text.toString()
            if (photoInfo.isNullOrBlank() || imagePath.isNullOrBlank()) { println(" Error") }
            else { mListImageDescription.add(photoInfo)  ;  createRV(mListImagePath, mListImageDescription) } } }

    private fun createRV(listimagePath: MutableList<String?>, listimageDescription: MutableList<String?>) {
        if (this.checkIsTablet()){
           create_recyclerview.layoutManager = GridLayoutManager(this,6)
            create_recyclerview.addItemDecoration(GridItemDecoration(10, 2))
            create_recyclerview.adapter =  CreateAdapter(listimagePath, listimageDescription, this)
            createData(mListImagePath, listimageDescription) }
        else {
            create_recyclerview.layoutManager = GridLayoutManager(this,2)
            create_recyclerview.addItemDecoration(GridItemDecoration(10, 2))
            create_recyclerview.adapter =  CreateAdapter(listimagePath, listimageDescription, this)
            createData(mListImagePath, listimageDescription)}
    }


    private fun saveImage(myBitmap: Bitmap) {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)
        val wallpaperDirectory = File((Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        if (!wallpaperDirectory.exists()) { wallpaperDirectory.mkdirs() }
        try { val f = Utils.createPathAndSave(wallpaperDirectory,bytes, this )
            createAlertDialog(f.absolutePath) } catch (e1: IOException) { e1.printStackTrace() } }

    private fun checkIsTablet(): Boolean {
        val display: Display = (this as Activity).windowManager.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        val widthInches: Float = metrics.widthPixels / metrics.xdpi
        val heightInches: Float = metrics.heightPixels / metrics.ydpi
        val diagonalInches = sqrt(widthInches.toDouble().pow(2.0) + heightInches.toDouble().pow(2.0))
        return diagonalInches >= 7.0
    }

    companion object {
        const val IMAGE_DIRECTORY = "/so"
        const val FILE_NAME = "My_Captured_Photo.jpg"
        const val PATH_PROVIDER   = "com.SofianeM.RealEstateManager.fileprovider"
        const val GET_CONTENT  = "android.intent.action.GET_CONTENT"
        const val MEDIA_PROVIDER  =   "com.android.providers.media.documents"
        const val DOWNLOAD_PROVIDER  =  "com.android.providers.downloads.documents"
        val listPerson = arrayOf("Leonardo", "Michel-Ange", "Raphael", "Donatello")
        val listType = arrayOf("Apartment", "House", "Loft", "Castle")
        val mListTypeOfLocation = arrayListOf("park", "supermarket", "pharmacy", "primary_school" )
        const val OPERATION_CAPTURE_PHOTO = 1
        const val OPERATION_CHOOSE_PHOTO = 2
        const val NEW_ID = "new_ID"

    }
}