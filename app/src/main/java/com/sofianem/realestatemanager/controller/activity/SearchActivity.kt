package com.sofianem.realestatemanager.controller.activity

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.format.Time
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import com.appyvet.materialrangebar.RangeBar
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.sofianem.realestatemanager.R
import com.sofianem.realestatemanager.controller.activity.CreateActivity.Companion.listPerson
import com.sofianem.realestatemanager.controller.activity.CreateActivity.Companion.listType
import com.sofianem.realestatemanager.utils.Utils
import com.sofianem.realestatemanager.viewmodel.MyViewModel
import com.sofianem.realestatemanager.viewmodel.MyViewModelForImages
import com.sofianem.realestatemanager.viewmodel.MyViewModelForPlaces
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class SearchActivity : AppCompatActivity(), LifecycleOwner  {
    private val mMyViewModel by viewModel<MyViewModel>()
    var mListAll = arrayListOf<Int>()
    //ADDRESS
    var mAddress: String? = ""; var mHintAddress = "" ; var mStreetNumber = "" ; var mRoute = ""
    // PRICE
    var mPriceMini = -1  ; var mPriceMax = 999999999
    // SURFACE
    var mSurfaceMini = -1 ; var mSurfaceMax = 999999
    // NB ROOM
    var mRoomMini = -1  ; var mRoomMax = 99
    // NB PHOTO
    var mPhotoMini: Int = -1  ; var mPhotoMax: Int = 99
    // DATE CREATE
    var mCreateDateBegin: Long = 1  ; var mCreateDateEnd: Long = 88888888870000
    // DATE END
    var mSoldDateBegin: Long = 1  ; var mSoldDateEnd: Long = 88888888870000
    // ALL
    var mType: String? = "" ; var mStatus: String = "" ; var mPerson: String? = ""
    // LOCATION
    var mPark: String? = "" ; var mPharmacy: String? = ""  ; var mSchool: String? = ""  ; var mMarket: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        mListAll.clear()
        searchPerson() ; searchRoom() ; searchType()
        searchSurface() ; searchPrice() ; searchAddress()
        searchStatus() ; initDateCreate() ; initDateSold()

        onClickHome() ; initPreSearch() }

    private fun searchStatus() {
        search_switch_sold.setOnCheckedChangeListener { c, _ ->
            if (c.isChecked) {
                println("-------CHECK---1 --" + c.toString())
                a_search_sold_dateEnd.isVisible = true
                a_search_sold_dateBegin.isVisible = true
                mStatus = "sold" }

            if (!c.isChecked) {
                println("-------CHECK---2 --" + c.toString())
                a_search_sold_dateEnd.visibility = View.INVISIBLE
                a_search_sold_dateBegin.visibility = View.INVISIBLE
                mStatus = "ok" } } }


    private fun searchAddress() {
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyByK0jz-yxjpZFX88W8zjzTwtzMtkPYC4w") }
        val autocompleteFragment = Utils.configureAutoCompleteFrag(supportFragmentManager, resources, this, mHintAddress)
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS_COMPONENTS))
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) { a_search_ed_adress.text = place.address
                place.addressComponents?.asList()?.forEach {
                    if (it.types.contains("street_number")) { mStreetNumber = it.name }
                    else if (it.types.contains("mRoute")) { mRoute = it.name } }
                a_search_ed_adress.text = "$mStreetNumber $mRoute"
                mAddress = "$mStreetNumber $mRoute" }

            override fun onError(p0: Status) { println("error") } }) }


    private fun searchSurface() {
        a_search_rangebar_surface.setOnRangeBarChangeListener(object : RangeBar.OnRangeBarChangeListener {
            override fun onTouchEnded(rangeBar: RangeBar?) {}
            override fun onRangeChangeListener(rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String, rightPinValue: String) {
                a_search_tx_minisurface.text = leftPinValue + "  Sq/ft"; a_search_tx_maxsurface.text = rightPinValue + "  Sq/ft"
                mSurfaceMini = leftPinValue.toInt() ; mSurfaceMax = rightPinValue.toInt() }

            override fun onTouchStarted(rangeBar: RangeBar?) {} }) }


    private fun searchType() {
        a_search_ed_type.setOnClickListener {
            val mBuilder1 = AlertDialog.Builder(this, R.style.MyDialogTheme)
            with(mBuilder1) { setItems(listType) { _, i ->
                a_search_ed_type.text = listType[i]
                mType = listType[i] }
                val mDialog = mBuilder1.create()
                mDialog.show() } } }


    private fun searchRoom() {
        a_search_ed_room_rangeBar.setOnRangeBarChangeListener(object :
            RangeBar.OnRangeBarChangeListener {
            override fun onTouchEnded(rangeBar: RangeBar?) {}
            override fun onRangeChangeListener(rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String, rightPinValue: String) {
                a_search_ed_room_mini.text = leftPinValue ; a_search_ed_room_max.text = rightPinValue
                mRoomMini = leftPinValue.toInt() ; mRoomMax = rightPinValue.toInt() }

            override fun onTouchStarted(rangeBar: RangeBar?) {} }) }

    fun searchPerson() {
        a_search_ed_personn.setOnClickListener {
            val mBuilder = AlertDialog.Builder(this, R.style.MyDialogTheme)
            with(mBuilder) {
                setItems(listPerson) { _, i ->
                    a_search_ed_personn.text = listPerson[i]
                    mPerson = listPerson[i]
                    initSearch() }
                val mDialog = mBuilder.create()
                mDialog.show() } } }


    private fun searchPrice() {
        a_search_rangebar_price.setOnRangeBarChangeListener(object :
            RangeBar.OnRangeBarChangeListener {
            override fun onTouchEnded(rangeBar: RangeBar?) {}
            override fun onTouchStarted(rangeBar: RangeBar?) {}
            override fun onRangeChangeListener(rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String?, rightPinValue: String?) {
                val valueLeft = leftPinValue.toString() + "0000"  ; val displayValueLeft = Utils.addWhiteSpace(valueLeft)
                val valueRight = rightPinValue.toString() + "0000"  ; val displayValueRight = Utils.addWhiteSpace(valueRight)
                a_search_tx_pricemini.text = displayValueLeft + "  $" ; a_search_tx_pricemax.text = displayValueRight + "  $"
                mPriceMini = valueLeft.toInt() ; mPriceMax = valueRight.toInt() } }) }


    private fun initPreSearch() { initPhoto() ; initMarket() ; initSchool() ; initPark() ; initPharmacy() ; initSearch() }


    private fun initPhoto() {
        a_search_ed_photo_rangebar.setOnRangeBarChangeListener(object :
            RangeBar.OnRangeBarChangeListener {
            override fun onTouchEnded(rangeBar: RangeBar?) {}
            override fun onRangeChangeListener(rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String?, rightPinValue: String?
            ) { a_search_ed_photo_mini.text = leftPinValue.toString(); a_search_ed_photo_max.text = rightPinValue.toString()
                mPhotoMini = leftPinValue!!.toInt() ; mPhotoMax = rightPinValue!!.toInt() }

            override fun onTouchStarted(rangeBar: RangeBar?) {} }) }

    private fun initMarket() { a_search_cb_market.setOnCheckedChangeListener { _, _ -> mMarket = "ok" } }

    private fun initSchool() { a_search_cb_school.setOnCheckedChangeListener { _, _ -> mSchool = "ok" } }

    private fun initPharmacy() { a_search_cb_pharmacy.setOnCheckedChangeListener { _, _ -> mPharmacy = "ok" } }

    private fun initPark() { a_search_cb_park.setOnCheckedChangeListener { _, _ -> mPark = "ok" } }

    private fun initDateSold() {
        a_search_sold_dateBegin.setOnClickListener {
            mSoldDateBegin = 1
            val dpd = OnDateSetListener { _, y, m, d -> a_search_sold_dateBegin.text = Utils.formatDate(y,m,d)
                mSoldDateBegin = Utils.convertToEpoch(Utils.formatDate(y,m,d)) }
            val now = Time()
            now.setToNow()
            val datePicker = DatePickerDialog(this, R.style.MyAppThemeCalendar, dpd, now.year, now.month, now.monthDay)
            datePicker.show()
            datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(resources.getColor(R.color.colorD))
            datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(resources.getColor(R.color.colorD)) }


        a_search_sold_dateEnd.setOnClickListener {
            mSoldDateEnd = Utils.formatDateV2(Date())
            val dpd = OnDateSetListener { _, y, m, d -> a_search_sold_dateEnd.text = Utils.formatDate(y,m,d)
                mSoldDateEnd = Utils.convertToEpoch(Utils.formatDate(y,m,d))}
            val now = Time()
            now.setToNow()
            val datePicker = DatePickerDialog(this, R.style.MyAppThemeCalendar, dpd, now.year, now.month, now.monthDay)
            datePicker.show()
            datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(resources.getColor(R.color.colorD))
            datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(resources.getColor(R.color.colorD)) } }

    private fun initDateCreate() {
        a_search_ed_dateBegin_create.setOnClickListener {
            val dpd = OnDateSetListener { _, y, m, d -> a_search_ed_dateBegin_create.text = Utils.formatDate(y, m, d)
                mCreateDateBegin = Utils.convertToEpoch(Utils.formatDate(y, m, d)) }
            val now = Time()
            now.setToNow()
            val datePicker2 = DatePickerDialog(this, R.style.MyAppThemeCalendar, dpd, now.year, now.month, now.monthDay)
            datePicker2.show()
            datePicker2.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(resources.getColor(R.color.colorD))
            datePicker2.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(resources.getColor(R.color.colorD))}

        a_search_ed_dateEnd_create.setOnClickListener {
            mCreateDateEnd = Utils.formatDateV2(Date())
            val dpd = OnDateSetListener { _, y, m, d -> a_search_ed_dateEnd_create.text = Utils.formatDate(y,m,d)
                mCreateDateEnd = Utils.convertToEpoch(Utils.formatDate(y,m,d))}
            val now = Time()
            now.setToNow()
            val datePicker = DatePickerDialog(this, R.style.MyAppThemeCalendar, dpd, now.year, now.month, now.monthDay)
            datePicker.show()
            datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(resources.getColor(R.color.colorD))
            datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(resources.getColor(R.color.colorD)) } }


    private fun onClickHome() {
        a_search_fb_home.setOnClickListener { val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) } }

    private fun initSearch() {

        a_search_fb_searchicon.setOnClickListener {
            if (mPerson == "" && mType == "" && mSurfaceMini == -1 && mSurfaceMax == 999999 && mPriceMini == -1 && mPriceMax == 999999999 && mRoomMini == -1
                && mRoomMax == 99 && mCreateDateBegin.toInt() == 1 && mCreateDateEnd == 88888888870000 && mPhotoMini == -1 && mPhotoMax == 99
                && mSoldDateBegin.toInt() == 1 && mSoldDateEnd == 88888888870000 && mStatus == "" && mPharmacy == "" && mSchool == "" && mMarket == "" && mPark == "") { mMyViewModel.allWordsLive.observe(this, androidx.lifecycle.Observer { it.forEach { op -> mListAll.add(op.id) } }) }
            else {
                if (mPerson == "") { mPerson = "%" } ; if (mType == "") { mType = "%" };  if (mStatus == "") { mStatus = "%" };   if (mPharmacy == "") { mPharmacy = "%" }
                if (mSchool == "") { mSchool = "%" }; if (mMarket == "") { mMarket = "%" }; if (mPark == "") { mPark = "%" }

                val a  = mMyViewModel.getSearchAll(mPerson, mType, mSurfaceMini, mSurfaceMax, mPriceMini, mPriceMax, mRoomMini,
                    mRoomMax, mCreateDateBegin, mCreateDateEnd, mPhotoMini, mPhotoMax, mSoldDateBegin, mSoldDateEnd, mStatus, mPharmacy, mSchool, mMarket, mPark)

                a?.forEach { mListAll.add(it) }

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("masterId", mListAll)
                startActivity(intent) } } }


    private fun loadRV(mListAll: ArrayList<Int>?) {
        a_search_fb_searchicon.setOnClickListener {
            if (mListAll.isNullOrEmpty()) {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent) }
            else { val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(MASTER_ID, mListAll)
                startActivity(intent) } } }


    companion object {
        const val MASTER_ID = "id"
    }

}
