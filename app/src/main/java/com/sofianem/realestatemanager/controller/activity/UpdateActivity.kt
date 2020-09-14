package com.sofianem.realestatemanager.controller.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.format.Time
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.databinding.adapters.ImageViewBindingAdapter.setImageDrawable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
import com.sofianem.realestatemanager.data.model.EstateR
import com.sofianem.realestatemanager.utils.GeocoderUtil
import com.sofianem.realestatemanager.utils.MyCommunicationForImage
import com.sofianem.realestatemanager.utils.Utils
import com.sofianem.realestatemanager.viewmodel.MyViewModel
import com.sofianem.realestatemanager.viewmodel.MyViewModelForImages
import kotlinx.android.synthetic.main.activity_upload.*
import kotlinx.android.synthetic.main.dialog_custom_layout.view.*
import kotlinx.android.synthetic.main.dialog_layout.view.*
import kotlinx.android.synthetic.main.dialog_number_picker.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


@Suppress("DEPRECATION")
class UpdateActivity : AppCompatActivity(), MyCommunicationForImage {

    private lateinit var mMyViewModel: MyViewModel
    private var mUri: Uri? = null
    private lateinit var mMyViewModelForImages: MyViewModelForImages
    private val OPERATION_CAPTURE_PHOTO = 1
    private val OPERATION_CHOOSE_PHOTO = 2
    private val imagePath: String? = ""
    lateinit var view: View
    var mId = 0
    var type: String = ""
    var city: String = ""
    var price: Int = 0
    var surface: Int = 0
    var number_of_room: Int = 0
    var description: String = ""
    var adress: String = ""
    var status: String = "ok"
    var mGeoLoc: String = ""
    var date_begin: Long = 3
    var date_end: Long = 8888888888
    var personn: String = ""
    var hintAdress: String = ""
    var listImage_id: MutableList<Int?> = arrayListOf()
    var listImage_path: MutableList<String?> = arrayListOf()
    var listImage_description: MutableList<String?> = arrayListOf()
    var mID: Int = 0
    lateinit var estateR: EstateR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        mMyViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)
        mMyViewModelForImages = ViewModelProviders.of(this).get(MyViewModelForImages::class.java)
        mID = intent.getIntExtra(ID, 1)

        retrieveData(mID)
        initRV()
        createRV(mID)
        saveEntry()


    }

    private fun retrieveData(id: Int) {
        mMyViewModel.allWordsLive.observe(this, androidx.lifecycle.Observer { list ->
            println(" ---------id ----2 -----" + id)
            println(" ---------id ----3 -----" + list[1])
            println(" ---------id ----4 -----" + id)
            var lstEst = list[id]

            if (lstEst.type == "") { upload_type.text = "-" }
            else { upload_type.text = lstEst.type; type = lstEst.type }

            if (lstEst.price == 0) { upload_tx_pric.text = "-" }
            else { upload_tx_pric.text = lstEst.price.toString() + "  $"; price = lstEst.price }

            if (lstEst.surface == 0) { upload_tx_surface.text = "-" }
            else { upload_tx_surface.text = lstEst.surface.toString() + "  Sq/ft"; surface = lstEst.surface }

            if (lstEst.number_of_room == 0) { upload_room.text = "-" }
            else { upload_room.text = lstEst.number_of_room.toString(); number_of_room = lstEst.number_of_room }

            if (lstEst.description == "") { upload_description.setText("-") }
            else { upload_description.setText(lstEst.description); description = lstEst.description }

            if (lstEst.personn == "") { upload_personn.text = "-" }
            else { upload_personn.text = lstEst.personn; personn = lstEst.personn }

            if (lstEst.location != "") { mGeoLoc = lstEst.location }

            if (lstEst.date_begin.toInt().equals(3)) { upload_datebegin.text = "-" }
            else { date_begin = lstEst.date_begin; upload_datebegin.text = Utils.convertToLocalB(lstEst)}

            if (lstEst.date_end == 8888888888) { upload_dateend.isChecked = false}
            else { upload_dateend.isChecked = true}


            mId = lstEst.id

            upload_city.text = lstEst.city

            city = lstEst.city

            hintAdress = lstEst.adress
            upload_adress.text = hintAdress

            upload_tx_pric.setOnClickListener { upload_rangebar_price.visibility = View.VISIBLE }
            upload_tx_surface.setOnClickListener { upload_rangebar_surface.visibility = View.VISIBLE }

            uploadData(lstEst)
            OnClick()
        })
    }

    private fun initRV() {
        val layoutManager = GridLayoutManager(this, 3)
        upload_recyclerview.layoutManager = layoutManager
        upload_recyclerview.adapter = UploadAdapter(listImage_path, listImage_description, listImage_id, this) }


    private fun createRV(mID: Int) {
        if (mID != null) {
            mMyViewModelForImages.allImageLive.observe(this, androidx.lifecycle.Observer { it ->
                it.forEach { value ->
                    if (mID == value.masterId) {
                        listImage_id.add(value.imageId)
                        listImage_path.add(value.imageUri)
                        listImage_description.add(value.imageDescription)
                        upload_recyclerview.adapter?.notifyDataSetChanged() } } }) } }


    private fun uploadData(est: EstateR) {
        activity_upload_saveData_floating.setOnClickListener {
            description = upload_description.text.toString().trim()

            est.type = type
            est.status = status
            est.city = city
            est.price = price
            est.surface = surface
            est.number_of_room = number_of_room
            est.description = description
            est.date_begin = date_begin
            est.date_end = date_end
            est.personn = personn
            est.adress = hintAdress
            est.location = mGeoLoc

            mMyViewModel.updateTodo(est)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    fun saveEntry() {
        load_city(); load_room(); load_personn(); load_date_begin(); load_date_end();load_price(); load_adress(); load_type(); load_surface()
    }

    private fun load_city() {
        city = upload_city.text.toString().trim()
    }

    private fun load_room() {
        upload_room.setOnClickListener {
            val d = Dialog(this)
            d.setContentView(R.layout.dialog_number_picker)
            d.numberPicker.maxValue = 15
            d.numberPicker.minValue = 1
            d.numberPicker.wrapSelectorWheel = false
            d.button_check_numberpicker.setOnClickListener {
                upload_room.text = d.numberPicker.value.toString()
                number_of_room = d.numberPicker.value
                d.dismiss()
            }
            d.show()
        } }

    private fun load_personn() {
        upload_personn.setOnClickListener {
            val view = this.currentFocus
            view?.let { v ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(v.windowToken, 0)
            }
            val mBuilder = AlertDialog.Builder(this, R.style.MyDialogTheme)
            with(mBuilder) {
                setItems(listPerson) { dialog, i ->
                    upload_personn.text = listPerson[i]
                    personn = upload_personn.text.toString().trim()
                    dialog.dismiss()
                }
                val mDialog = mBuilder.create()
                mDialog.show()
            } } }

    private fun load_date_begin() {
        upload_datebegin.setOnClickListener {
            val dpd = DatePickerDialog.OnDateSetListener { view, y, m, d ->
                    upload_datebegin.setText(Utils.formatDate(y, m, d))
                    date_begin = Utils.convertToEpoch(Utils.formatDate(y, m, d)) }
            val now = android.text.format.Time(); now.setToNow()
            val d = DatePickerDialog(this, R.style.MyAppThemeCalendar, dpd, now.year, now.month, now.monthDay)
            d.show(); d.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setBackgroundColor(resources.getColor(R.color.colorD))
            d.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setBackgroundColor(resources.getColor(R.color.colorD))
        } }

    private fun load_date_end() {
        upload_dateend.setOnCheckedChangeListener { _, b ->

            if (b) {
                upload_recyclerview_cache.isVisible = true
                val now = Time()
                now.setToNow()
                val a = now.monthDay
                val b1 = now.month
                val c = now.year
                date_end =  Utils.convertToEpoch(Utils.formatDate(c,b1,a))
                status = "sold"
                }

            else {   upload_recyclerview_cache.isVisible = false
                date_end = 8888888888
                status = "ok" }

        }
    }

    private fun load_price() {
        upload_rangebar_price.setOnRangeBarChangeListener(object :
            RangeBar.OnRangeBarChangeListener {
            override fun onTouchEnded(rangeBar: RangeBar?) {}
            override fun onRangeChangeListener(
                rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String?, rightPinValue: String?) {
                val value = rightPinValue.toString() + "0000"
                val displayValue = Utils.addWhiteSpace(value)
                upload_tx_pric.text = displayValue + "  $"; price = value.toInt() }
            override fun onTouchStarted(rangeBar: RangeBar?) {}
        }) }

    private fun load_adress() {
        var streetNumber = ""
        var route = ""
        if (!Places.isInitialized()) { Places.initialize(applicationContext, "AIzaSyByK0jz-yxjpZFX88W8zjzTwtzMtkPYC4w") }
        var autocompleteFragment = Utils.configureAutoCompleteFrag(supportFragmentManager, resources, this, hintAdress)
        autocompleteFragment?.setPlaceFields(
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS_COMPONENTS))
        autocompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                upload_adress.text = place.address
                place.addressComponents?.asList()?.forEach {
                    if (it.types.contains("street_number")) { streetNumber = it.name }
                    else if (it.types.contains("route")) { route = it.name }
                    else if (it.types.contains("locality")) { upload_city.setText(it.name); city = it.name } }
                hintAdress = "$streetNumber $route"
                mGeoLoc = GeocoderUtil.getlocationForListv2(hintAdress, city, this@UpdateActivity) }

            override fun onError(p0: Status) {}
        }) }


    private fun load_type() {
        upload_type.setOnClickListener {
            val mBuilder = AlertDialog.Builder(this, R.style.MyDialogTheme)
            with(mBuilder) {
                setItems(listType) { dialog, i ->
                    upload_type.text = listType[i]; type = listType[i]; dialog.dismiss() }
                val mDialog = mBuilder.create()
                mDialog.show()
            } } }

    private fun load_surface() {
        upload_rangebar_surface.setOnRangeBarChangeListener(object :
            RangeBar.OnRangeBarChangeListener {
            override fun onTouchEnded(rangeBar: RangeBar?) {}
            override fun onRangeChangeListener(
                rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String?, rightPinValue: String?) {
                upload_tx_surface.text = rightPinValue.toString() + "  Sq/ft"; surface =
                    rightPinValue!!.toInt() }

            override fun onTouchStarted(rangeBar: RangeBar?) {}
        }) }

    fun OnClick() {
        activity_upload_addPhoto_floating.setOnClickListener { selectImage() }
    }

    private fun selectImage() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(mDialogView)
        val mAlertDialog = builder.show()
        mDialogView.dialog_gallery.setOnClickListener { v ->
            mAlertDialog.dismiss()
            val checkExtStorage = Utils.CheckExternalStorage(this)
            if (checkExtStorage != PackageManager.PERMISSION_GRANTED) {
                Utils.askForStorage(this) }
            else { openGallery() }
        }

        mDialogView.dialog_photo.setOnClickListener { v ->
            mAlertDialog.dismiss()
            val checkCamera = Utils.CheckCamera(this)
            if (checkCamera != PackageManager.PERMISSION_GRANTED) { Utils.askForCamera(this) }
            else { capturePhoto() }
        }

        mDialogView.dialog_cancel.setOnClickListener {
            mAlertDialog.dismiss()
            Toast.makeText(applicationContext, "cancelled", Toast.LENGTH_SHORT).show()
        } }


    private fun capturePhoto() {
        val capturedImage = File(externalCacheDir, CreateActivity.FILE_NAME)
        if (capturedImage.exists()) { capturedImage.delete() }
        capturedImage.createNewFile()
        mUri = if (Build.VERSION.SDK_INT >= 24) {
            FileProvider.getUriForFile(this, CreateActivity.PATH_PROVIDER, capturedImage)
        } else { Uri.fromFile(capturedImage) }
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
        startActivityForResult(intent, OPERATION_CAPTURE_PHOTO)
    }

    private fun openGallery() {
        val intent = Intent(CreateActivity.GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, OPERATION_CHOOSE_PHOTO)
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
            OPERATION_CAPTURE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap =
                        BitmapFactory.decodeStream(contentResolver.openInputStream(mUri))
                    saveImage(bitmap)
                }

            OPERATION_CHOOSE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    var imagePath: String? = null;
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
        listImage_path.add(imagePath)
        val mDialogViewForImageInfo = LayoutInflater.from(this).inflate(R.layout.dialog_custom_layout, null)
        var builderForImageInfo = AlertDialog.Builder(this)
        builderForImageInfo.setView(mDialogViewForImageInfo)
        val mAlertDialogForImageInfo = builderForImageInfo.show()
        mDialogViewForImageInfo.custom_dialog_txt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (start == 0 )
                { mDialogViewForImageInfo.custom_dialog_ok.isVisible = false
                    mDialogViewForImageInfo.custom_dialog_not_ok.isVisible = true}
                else {
                mDialogViewForImageInfo.custom_dialog_ok.isVisible = true
                mDialogViewForImageInfo.custom_dialog_not_ok.isVisible = false}
            } })

        mDialogViewForImageInfo.custom_dialog_not_ok.setOnClickListener {
            Toast.makeText(this, " Please add description...", Toast.LENGTH_SHORT).show() }
        mDialogViewForImageInfo.custom_dialog_ok.setOnClickListener {
            mAlertDialogForImageInfo.dismiss()
            val photo_info: String? = mDialogViewForImageInfo.custom_dialog_txt.text.toString()
            if (imagePath != "" && photo_info != "") {
                mMyViewModelForImages.upadeSingleImageData(mId, imagePath, photo_info) }
            refreshRV()
        }
    }


    override fun deleteImage(IdImage: Int) {
        var listimg = mMyViewModelForImages.allImageLive
        listimg.value?.forEach { img ->
            if (IdImage == img.imageId) { mMyViewModelForImages.deleteImage(img) } }
        refreshRV() }


    override fun uploadImage(IdImage: Int) {
        var listimg = mMyViewModelForImages.allImageLive

        listimg.value?.forEach { img ->
            if (IdImage == img.imageId) {
                val mDialogViewForImageInfo =
                    LayoutInflater.from(this).inflate(R.layout.dialog_custom_layout, null)
                var builderForImageInfo = AlertDialog.Builder(this)
                builderForImageInfo.setView(mDialogViewForImageInfo)
                mDialogViewForImageInfo.custom_dialog_txt.setText(img.imageDescription)
                val mAlertDialogForImageInfo = builderForImageInfo.show()
                mDialogViewForImageInfo.custom_dialog_ok.setOnClickListener {
                    mAlertDialogForImageInfo.dismiss()
                    img.imageDescription = mDialogViewForImageInfo.custom_dialog_txt.text.toString()
                    mMyViewModelForImages.UpdateImageDes(img)
                    refreshRV()
                } } } }

     fun refreshRV(){
        mMyViewModelForImages.allImageLive.observe(this, Observer { listImg ->
            listImage_path.clear()
            listImage_description.clear()
            listImg.forEach {img ->
                if (mID == img.masterId) {
                    listImage_id.add(img.imageId)
                    listImage_path.add(img.imageUri)
                    listImage_description.add(img.imageDescription)
                    upload_recyclerview.adapter?.notifyDataSetChanged() }
            } }) }

    companion object {}
}



