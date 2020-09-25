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
import com.sofianem.realestatemanager.controller.activity.CreateActivity.Companion.listPerson
import com.sofianem.realestatemanager.controller.activity.CreateActivity.Companion.listType
import com.sofianem.realestatemanager.controller.activity.MainActivity.Companion.ID
import com.sofianem.realestatemanager.controller.adapter.UploadAdapter
import com.sofianem.realestatemanager.controller.view.GridItemDecoration
import com.sofianem.realestatemanager.data.model.EstateR
import com.sofianem.realestatemanager.data.model.ImageV
import com.sofianem.realestatemanager.utils.MyCommunicationForImage
import com.sofianem.realestatemanager.utils.Utils
import com.sofianem.realestatemanager.viewmodel.MyViewModel
import com.sofianem.realestatemanager.viewmodel.MyViewModelForImages
import kotlinx.android.synthetic.main.activity_upload.*
import kotlinx.android.synthetic.main.dialog_custom_layout.*
import kotlinx.android.synthetic.main.dialog_custom_layout.view.*
import kotlinx.android.synthetic.main.dialog_layout.*
import kotlinx.android.synthetic.main.dialog_layout.view.*
import kotlinx.android.synthetic.main.dialog_number_picker.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt


@Suppress("DEPRECATION")
class UpdateActivity : AppCompatActivity(), MyCommunicationForImage {
    private val mMyViewModel by viewModel<MyViewModel>()
    private val mMyViewModelForImages by viewModel<MyViewModelForImages>()
    private var mUri: Uri? = null
    private val mOpeCapturePhoto = 1
    private val mOpeChoosePhoto = 2
    private var imagePath: String? = ""
    lateinit var view: View
    var mId = 0
    private var mType: String = ""
    var mCity: String = ""
    var mPrice: Int = 0
    var mSurface: Int = 0
    private var mNumberOfRoom: Int = 0
    private var mDescription: String = ""
    private var mStatus: String = "ok"
    var mGeoLoc: String = ""
    private var mDateCreate: Long = 3
    private var mDateSold: Long = 8888888888
    private var mPersonn: String = ""
    var mAdress: String = ""
    private var mListImageId: MutableList<Int?> = arrayListOf()
    private var mListImagePath: MutableList<String?> = arrayListOf()
    private var mListImagemDescription: MutableList<String?> = arrayListOf()
    private var mID: Int = 0
    private var mNbPhoto:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        val iid = intent.getIntExtra(ID, 1)
        mID = iid -1
        retrieveData(mID)
        initRV()
        createRV(iid)
        saveEntry()
        clickHome() }

    private fun clickHome() {
        activity_upload_home_floating.setOnClickListener {
        val intent = Intent(this , MainActivity::class.java)
        startActivity(intent)} }

    private fun retrieveData(id: Int) {
        mMyViewModel.mAllEstate.observe(this, androidx.lifecycle.Observer { list ->
            val lstEst = list[id]

            if (lstEst.type == "") { upload_type.text =  "     -     " }
            else { upload_type.text = lstEst.type; mType = lstEst.type }

            if (lstEst.price == 0) { upload_tx_pric.text =  "     -     " }
            else {
                val mDisplayPrice = lstEst.price.toString() + "  $"
                upload_tx_pric.text = mDisplayPrice; mPrice = lstEst.price }

            if (lstEst.surface == 0) { upload_tx_surface.text =  "     -     " }
            else {
                val mDisplaySurface = lstEst.surface.toString() + "  Sq/ft"
                    upload_tx_surface.text = mDisplaySurface  ; mSurface = lstEst.surface }

            if (lstEst.number_of_room == 0) { upload_room.text = "     -     "}
            else {
                upload_room.text = lstEst.number_of_room.toString(); mNumberOfRoom = lstEst.number_of_room }

            if (lstEst.description == "") { upload_description.setText( "     -     ") }
            else { upload_description.setText(lstEst.description); mDescription = lstEst.description }

            if (lstEst.personn == "") { upload_personn.text =  "     -     " }
            else { upload_personn.text = lstEst.personn; mPersonn = lstEst.personn }

            if (lstEst.location != "") { mGeoLoc = lstEst.location }

            if (lstEst.date_begin.toInt() == 3) { upload_datebegin.text =  "     -     " }
            else { mDateCreate = lstEst.date_begin; upload_datebegin.text = Utils.convertToLocalB(lstEst)}

            if (lstEst.date_end == 8888888888) { upload_dateSold.text =  "     -     "
                cancel_sold.visibility = View.INVISIBLE
            }
            else {
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                mDateSold = lstEst.date_begin
                val mDisplayDateEnd = sdf.format(Date(mDateSold))
                upload_dateSold.text = mDisplayDateEnd.toString()
                cancel_sold.visibility = View.VISIBLE
                upload_recyclerview_cache.visibility = View.VISIBLE

            }

         //   upload_dateend.isChecked = lstEst.date_end != 8888888888

            mId = lstEst.id
            upload_city.text = lstEst.city
            mCity = lstEst.city
            mAdress = lstEst.adress
            upload_adress.text = mAdress

            upload_tx_pric.setOnClickListener { upload_rangebar_price.visibility = View.VISIBLE }
            upload_tx_surface.setOnClickListener { upload_tx_surface.visibility = View.VISIBLE }

            uploadData(lstEst)
            onClick()
        }) }

    private fun initRV() = if (this.checkIsTablet()){
        this.upload_recyclerview.layoutManager = GridLayoutManager(this,6)
        this.upload_recyclerview.addItemDecoration(GridItemDecoration(10, 2))}
    else{
        this.upload_recyclerview.layoutManager = GridLayoutManager(this,2)
        this.upload_recyclerview.addItemDecoration(GridItemDecoration(10, 2)) }


    private fun createRV(mID: Int) {
        if (mID != null) { mMyViewModelForImages.allImageLive.observe(this, androidx.lifecycle.Observer {
            it.forEach { value -> if (mID == value.masterId) {
                        mListImageId.add(value.imageId)
                        mListImagePath.add(value.imageUri)
                        mListImagemDescription.add(value.imageDescription) } }
                upload_recyclerview.adapter = UploadAdapter(mListImagePath, mListImagemDescription, mListImageId, this) }) } }


    private fun uploadData(est: EstateR) {
        activity_upload_saveData_floating.setOnClickListener {
            mNbPhoto = mListImagePath.size
            mDescription = upload_description.text.toString().trim()
            est.type = mType
            est.status = mStatus
            est.city = mCity
            est.price= mPrice
            est.surface = mSurface
            est.number_of_room = mNumberOfRoom
            est.description = mDescription
            est.date_begin = mDateCreate
            est.date_end = mDateSold
            est.personn = mPersonn
            est.adress = mAdress
            est.location = mGeoLoc
            est.nb_photo = mNbPhoto

            mMyViewModel.updateTodo(est)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
           finish()
        }
    }


    private fun saveEntry() { loadCity(); loadRoom(); loadPersonn(); loadDateCreate(); loadDateSold();loadPrice(); loadAdress(); loadType(); loadSurface() }

    private fun loadCity() { mCity = upload_city.text.toString().trim()
    }

    private fun loadRoom() {
        upload_room.setOnClickListener {
            val d = Dialog(this)
            d.requestWindowFeature(Window.FEATURE_NO_TITLE)
            d.window.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
            d.setContentView(R.layout.dialog_number_picker)
            d.numberPicker.maxValue = 15
            d.numberPicker.minValue = 1
            d.numberPicker.wrapSelectorWheel = false
            d.button_check_numberpicker.setOnClickListener {
                upload_room.text = d.numberPicker.value.toString()
                mNumberOfRoom = d.numberPicker.value
                d.dismiss() }
            d.show() } }

    private fun loadPersonn() {
        upload_personn.setOnClickListener {
            val view = this.currentFocus
            view?.let { v ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(v.windowToken, 0) }
            val mBuilder = AlertDialog.Builder(this, R.style.MyDialogTheme)
            with(mBuilder) {
                setItems(listPerson) { dialog, i ->
                    upload_personn.text = listPerson[i]
                    mPersonn = upload_personn.text.toString().trim()
                    dialog.dismiss() }
                val mDialog = mBuilder.create()
                mDialog.show() } } }

    private fun loadDateCreate() {
        upload_datebegin.setOnClickListener {
            val dpd = DatePickerDialog.OnDateSetListener { view, y, m, d ->
                    upload_datebegin.text = Utils.formatDate(y, m, d)
                    mDateCreate = Utils.convertToEpoch(Utils.formatDate(y, m, d)) }
            val now = Time(); now.setToNow()
            val d = DatePickerDialog(this, R.style.MyAppThemeCalendar, dpd, now.year, now.month, now.monthDay)
            d.show(); d.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setBackgroundColor(resources.getColor(R.color.colorD))
            d.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setBackgroundColor(resources.getColor(R.color.colorD)) } }

    private fun loadDateSold() {
        upload_dateSold.setOnClickListener {
            val dpd = DatePickerDialog.OnDateSetListener { view, y, m, d ->
                upload_dateSold.text = Utils.formatDate(y, m, d)
                mDateSold = Utils.convertToEpoch(Utils.formatDate(y, m, d))
                mStatus = "sold"
                cancel_sold.visibility = View.VISIBLE
                upload_recyclerview_cache.visibility = View.VISIBLE}
            val now = Time(); now.setToNow()
            val d = DatePickerDialog(this, R.style.MyAppThemeCalendar, dpd, now.year, now.month, now.monthDay)
            d.show()
            d.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(resources.getColor(R.color.colorD))
            d.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(resources.getColor(R.color.colorD))
             }

        cancel_sold.setOnClickListener {
            cancel_sold.visibility = View.INVISIBLE
            upload_recyclerview_cache.visibility = View.INVISIBLE
            mStatus = "ok"
            upload_dateSold.text = "-"
            mDateSold = 8888888888 }

    }

    private fun loadPrice() {
        upload_rangebar_price.setOnRangeBarChangeListener(object :
            RangeBar.OnRangeBarChangeListener {
            override fun onTouchEnded(rangeBar: RangeBar?) {}
            override fun onRangeChangeListener(
                rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String?, rightPinValue: String?) {
                val value = rightPinValue.toString() + "0000"
                val mDisplayPrice = Utils.addWhiteSpace(value) + " $"
                upload_tx_pric.text = mDisplayPrice
                mPrice = value.toInt() }
            override fun onTouchStarted(rangeBar: RangeBar?) {} }) }

    private fun loadAdress() {
        var streetNumber = ""
        var route = ""
        if (!Places.isInitialized()) { Places.initialize(applicationContext, "AIzaSyByK0jz-yxjpZFX88W8zjzTwtzMtkPYC4w") }
        val autocompleteFragment = Utils.configureAutoCompleteFrag(supportFragmentManager, resources, this, mAdress)
        autocompleteFragment.setPlaceFields(
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS_COMPONENTS))
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                upload_adress.text = place.address

                place.addressComponents?.asList()?.forEach {
                    when {
                        it.types.contains("street_number") -> { streetNumber = it.name }
                        it.types.contains("route") -> { route = it.name }
                        it.types.contains("locality") -> {
                            upload_city.text = it.name; mCity = it.name }
                    }
                }
                mAdress = "$streetNumber $route"
                mGeoLoc = Utils.getlocationForList(mAdress, mCity, this@UpdateActivity)
            }

            override fun onError(p0: Status) {} })
    }


    private fun loadType() {
        upload_type.setOnClickListener {
            val mBuilder = AlertDialog.Builder(this, R.style.MyDialogTheme)
            mBuilder.setItems(listType) { dialog, i ->
                upload_type.text = listType[i]; mType = listType[i]; dialog.dismiss() }
            val mDialog = mBuilder.create()
            mDialog.show()
        } }

    private fun loadSurface() {
        upload_rangebar_surface.setOnRangeBarChangeListener(object :
            RangeBar.OnRangeBarChangeListener {
            override fun onTouchEnded(rangeBar: RangeBar?) {}
            override fun onRangeChangeListener(
                rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String?, rightPinValue: String?) {
                val mDisplaySurface = rightPinValue.toString() + "  Sq/ft"
                upload_tx_surface.text = mDisplaySurface
                mSurface = rightPinValue!!.toInt() }

            override fun onTouchStarted(rangeBar: RangeBar?) {} }) }

    private fun onClick() {
        activity_upload_addPhoto_floating.setOnClickListener { selectImage() }
    }

    private fun selectImage() {
        val d = Dialog(this)
        d.requestWindowFeature(Window.FEATURE_NO_TITLE)
        d.window.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
        d.setContentView(R.layout.dialog_layout)
        d.dialog_gallery.setOnClickListener {
            d.dismiss()
            val checkExtStorage = Utils.checkExternalStorage(this)
            if (checkExtStorage != PackageManager.PERMISSION_GRANTED) {
                Utils.askForStorage(this) }
            else { openGallery() }
        }

        d.dialog_photo.setOnClickListener { v ->
            d.dismiss()
            val checkCamera = Utils.checkCamera(this)
            if (checkCamera != PackageManager.PERMISSION_GRANTED) { Utils.askForCamera(this) }
            else { capturePhoto() }
        }

        d.dialog_cancel.setOnClickListener {
            d.dismiss()
            Toast.makeText(applicationContext, "cancelled", Toast.LENGTH_SHORT).show() }

    d.show()}



    private fun capturePhoto() {
        val capturedImage = File(externalCacheDir, CreateActivity.FILE_NAME)
        if (capturedImage.exists()) { capturedImage.delete() }
        capturedImage.createNewFile()
        mUri = if (Build.VERSION.SDK_INT >= 24) {
            FileProvider.getUriForFile(this, CreateActivity.PATH_PROVIDER, capturedImage)
        } else { Uri.fromFile(capturedImage) }
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
        startActivityForResult(intent, mOpeCapturePhoto)
    }

    private fun openGallery() {
        val intent = Intent(CreateActivity.GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, mOpeChoosePhoto)
    }


    private fun getImagePath(uri: Uri?, selection: String?): String {
        var path: String? = null
        val cursor = contentResolver.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) { path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)) }
            cursor.close()
        }
        return path!!
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantedResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantedResults)
        when (requestCode) {
            1 -> if (grantedResults.isNotEmpty() && grantedResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery() } else { Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show() }

            2 -> if (grantedResults.isNotEmpty() && grantedResults[0] == PackageManager.PERMISSION_GRANTED) {
                capturePhoto() } else { Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show() }
        } }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            mOpeCapturePhoto ->
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap =
                        BitmapFactory.decodeStream(contentResolver.openInputStream(mUri))
                    saveImage(bitmap)
                }

            mOpeChoosePhoto ->
                if (resultCode == Activity.RESULT_OK) {
                    var imagePath: String? = null
                    val uri = data!!.data
                    if (DocumentsContract.isDocumentUri(this, uri)) {
                        val docId = DocumentsContract.getDocumentId(uri)
                        if (CreateActivity.MEDIA_PROVIDER == uri.authority) {
                            imagePath = Utils.createPathForMedia(docId, this)
                        } else if (CreateActivity.DOWNLOAD_PROVIDER == uri.authority) {
                            imagePath = Utils.createPathForDownload(docId, this)
                        }
                    } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                        imagePath = getImagePath(uri, null)
                    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                        imagePath = uri.path
                    }

                    renderImage(imagePath)
                    createAlertDialog(imagePath)
                }
        }
    }

    private fun saveImage(myBitmap: Bitmap) {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)
        val wallpaperDirectory =
            File((Environment.getExternalStorageDirectory()).toString() + CreateActivity.IMAGE_DIRECTORY)
        if (!wallpaperDirectory.exists()) { wallpaperDirectory.mkdirs() }
        try { val f = Utils.createPathAndSave(wallpaperDirectory, bytes, this)
            createAlertDialog(f.absolutePath)
        } catch (e1: IOException) { e1.printStackTrace() }
    }

    private fun renderImage(imagePath: String?) {
        if (imagePath != null) { val bitmap = BitmapFactory.decodeFile(imagePath) } else {
            Toast.makeText(this, "ImagePath == null", Toast.LENGTH_SHORT).show() }
    }

    private fun createAlertDialog(imagePath: String?) {

        val d = Dialog(this)
        d.requestWindowFeature(Window.FEATURE_NO_TITLE)
        d.window.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
        d.setContentView(R.layout.dialog_custom_layout)
       d.show()
        d.custom_dialog_txt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                println("s----$s---start--$start--before---$before--count---$count")
                if (start == 0 )
                { d.custom_dialog_ok.isVisible = false
                    d.custom_dialog_not_ok.isVisible = true}
                else {
                d.custom_dialog_ok.isVisible = true
                d.custom_dialog_not_ok.isVisible = false}
            } })

        d.custom_dialog_not_ok.setOnClickListener {
            Toast.makeText(this, " Please add mDescription...", Toast.LENGTH_SHORT).show() }

        d.custom_dialog_ok.setOnClickListener {
            mListImagePath.clear()
            mListImagemDescription.clear()
            mListImageId.clear()
            d.dismiss()
            val mPhotoInfo: String? = d.custom_dialog_txt.text.toString()
            if (imagePath != "" && mPhotoInfo != "") {
                mMyViewModelForImages.upadeSingleImageData(mId, imagePath, mPhotoInfo)
              }
        }
    }


    override fun deleteImage(IdImage: Int, mDesc: String, mPath: String) {
        mListImagePath.clear()
        mListImagemDescription.clear()
        mListImageId.clear()
        mMyViewModelForImages.deleteImageById(IdImage) }


    override fun uploadImage(IdImage: Int) {
        val listimg = mMyViewModelForImages.allImageLive

        listimg.value?.forEach { img ->
            if (IdImage == img.imageId) {
                imagePath = img.imageUri
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
                    Toast.makeText(this, " Please add mDescription...", Toast.LENGTH_SHORT).show() }

                d.custom_dialog_ok.setOnClickListener {
                    mListImagePath.clear()
                    mListImagemDescription.clear()
                    mListImageId.clear()
                    d.dismiss()
                    val mPhotoInfo: String? = d.custom_dialog_txt.text.toString()
                    if (imagePath != "" && mPhotoInfo != "") {
                       val imgV = ImageV()
                        imgV.imageDescription = mPhotoInfo
                        imgV.imageUri = imagePath
                        imgV.masterId = mId
                        imgV.imageId = IdImage
                        mMyViewModelForImages.updateImageDes(imgV) } }}}}


    private fun checkIsTablet(): Boolean {
        val display: Display = (this as Activity).windowManager.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        val widthInches: Float = metrics.widthPixels / metrics.xdpi
        val heightInches: Float = metrics.heightPixels / metrics.ydpi
        val diagonalInches = sqrt(
            widthInches.toDouble().pow(2.0) + heightInches.toDouble().pow(2.0)
        )
        return diagonalInches >= 7.0
    }

    companion object
}



