package com.sofianem.realestatemanager.controller.activity

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.format.Time
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.appyvet.materialrangebar.RangeBar
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.sofianem.realestatemanager.R
import com.sofianem.realestatemanager.utils.Utils
import com.sofianem.realestatemanager.viewmodel.MyViewModel
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


@Suppress("DEPRECATION")
class SearchActivity : AppCompatActivity(), LifecycleOwner  {
    private val mMyViewModel by viewModel<MyViewModel>()
    val mListIdForAdress: ArrayList<Int> = ArrayList()
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
    var mType: String? = "%" ; var mStatus: String = "%" ; var mPerson: String? = "%"
    // LOCATION
    var mPark: String? = "%" ; var mPharmacy: String? = "%"  ; var mSchool: String? = "%"  ; var mMarket: String? = "%"
    // CITY
    var mCity: String? = "%" ;
    // LOCATION
    var mLocation: String? = "%" ;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        mMyViewModel.mAllEstateId.observe(this, androidx.lifecycle.Observer {mList ->
            mList.forEach {mID -> mListIdForAdress.add(mID) } })

        initSchool()
        searchPerson() ; searchRoom() ; searchType()
        searchSurface() ; searchPrice() ; searchAddress()
        searchStatus() ; initDateCreate() ; initDateSold()
        initMarket()
        initPark()
        initPharmacy()

        initPhoto()
        initSearch()
        onClickHome()  }

    private fun searchStatus() {
        search_switch_sold.setOnCheckedChangeListener { c, _ ->
            if (c.isChecked) {
                a_search_sold_dateEnd.visibility = View.VISIBLE
                a_search_sold_dateBegin.visibility = View.VISIBLE
                txtSoldDate1.visibility = View.VISIBLE
                txtSoldDate2.visibility = View.VISIBLE
                mStatus = "sold"
                initSearch()}

            if (!c.isChecked) {
                a_search_sold_dateEnd.visibility = View.INVISIBLE
                a_search_sold_dateBegin.visibility = View.INVISIBLE
                txtSoldDate1.visibility = View.INVISIBLE
                txtSoldDate2.visibility = View.INVISIBLE
                mStatus = "%"
                mSoldDateBegin = 1
                mSoldDateEnd = 88888888870000
                initSearch()} } }


    private fun searchAddress() {
        var mStreetNumber = ""
        var mStreetName = ""
        var mCity = ""
        if (!Places.isInitialized()) { Places.initialize(applicationContext, "AIzaSyByK0jz-yxjpZFX88W8zjzTwtzMtkPYC4w") }

        val autocompleteFragment = Utils.configureAutoCompleteFragV2(supportFragmentManager, resources, this, "Adress")
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                a_search_ed_adress.text = place.address
                place.addressComponents?.asList()?.forEach { mAdressComp ->
                    Log.i("TAG", "AutoComplet: " + mAdressComp.types + " " )
                    if (mAdressComp.types.contains("street_number")) { mStreetNumber = mAdressComp.name }
                    else if (mAdressComp.types.contains("route")) { mStreetName = mAdressComp.name }
                    else if (mAdressComp.types.contains("locality")) { mCity = mAdressComp.name } }

                a_search_ed_adress.text = "$mStreetNumber $mStreetName, $mCity "
                a_search_ed_adress.visibility = View.VISIBLE
                mAddress = "$mStreetNumber $mStreetName"

                mLocation = Utils.mLatLngString(place.latLng)
                checkDistance(mLocation!!)
            }
            override fun onError(status: Status) { Log.i("TAG", "An error occurred: $mStatus")
            } }) }

    private fun checkDistance(mGeoLocSearch: String) {
        mMyViewModel.mAllEstateId.observe(this, androidx.lifecycle.Observer { mListID ->
            mListIdForAdress.clear()
            mListID?.forEach { mId ->
                val mGeoLocItem =  mMyViewModel.getGeoLocById(mId)
                val mSearchLat = Utils.currentLat(mGeoLocSearch)
                val mSearchLng = Utils.currentLng(mGeoLocSearch)
                val mItemLat = Utils.currentLat(mGeoLocItem)
                val mItemLng = Utils.currentLng(mGeoLocItem)

                val mDistance = Utils.calculateDistance(mSearchLat, mSearchLng, mItemLat, mItemLng).roundToInt()
                if (mDistance < 700) {mListIdForAdress.add(mId)}

                println( " ------>>>>>> DISTANCE <<<<<------ / ID /  $mId  /   $mDistance ")
            } // FOREACH
            initSearch()
        })



    }


    private fun searchSurface() {
        a_search_rangebar_surface.setOnRangeBarChangeListener(object : RangeBar.OnRangeBarChangeListener {
            override fun onTouchEnded(rangeBar: RangeBar?) {}
            override fun onRangeChangeListener(rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String, rightPinValue: String) {
                a_search_tx_minisurface.text = leftPinValue + "  Sq/ft"; a_search_tx_maxsurface.text = rightPinValue + "  Sq/ft"
                mSurfaceMini = leftPinValue.toInt() ; mSurfaceMax = rightPinValue.toInt()
                initSearch()}

            override fun onTouchStarted(rangeBar: RangeBar?) {} }) }


    private fun searchRoom() {
        a_search_ed_room_rangeBar.setOnRangeBarChangeListener(object :
            RangeBar.OnRangeBarChangeListener {
            override fun onTouchEnded(rangeBar: RangeBar?) {}
            override fun onRangeChangeListener(rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String, rightPinValue: String) {
                a_search_ed_room_mini.text = leftPinValue ; a_search_ed_room_max.text = rightPinValue
                mRoomMini = leftPinValue.toInt() ; mRoomMax = rightPinValue.toInt()
                if (mRoomMini == 1 && mRoomMax == 12)
                {  mRoomMini = -1; mRoomMax = 99 }
                initSearch()}

            override fun onTouchStarted(rangeBar: RangeBar?) {} }) }

    fun searchPerson() {
        val mSelectPersonn = resources.getStringArray(((R.array.Person)))
        if (spPersonn != null) { spPersonn.background.setColorFilter(resources.getColor(R.color.colorD), PorterDuff.Mode.SRC_ATOP);
            val adapter = ArrayAdapter(this, R.layout.spinner_custom, mSelectPersonn)
            spPersonn.adapter = adapter}

        spPersonn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, i: Int, id: Long) {
                if (mSelectPersonn[i] == "Person") {mPerson = "%";   initSearch() }
                else { mPerson = mSelectPersonn[i]; initSearch()} }

            override fun onNothingSelected(parent: AdapterView<*>) {} } }


    private fun searchType() {
        val mSelectType = resources.getStringArray(((R.array.Type)))
        if (spType != null) { spType.background.setColorFilter(resources.getColor(R.color.colorD), PorterDuff.Mode.SRC_ATOP);
            val adapter = ArrayAdapter(this, R.layout.spinner_custom, mSelectType)
            spType.adapter = adapter}

        spType.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, i: Int, id: Long) {
                if (mSelectType[i] == "Type") {mType = "%";   initSearch()}
                else { mType = mSelectType[i]; initSearch() } }

            override fun onNothingSelected(parent: AdapterView<*>) {} } }


    private fun searchPrice() {
        a_search_rangebar_price.setOnRangeBarChangeListener(object :
            RangeBar.OnRangeBarChangeListener {
            override fun onTouchEnded(rangeBar: RangeBar?) {}
            override fun onTouchStarted(rangeBar: RangeBar?) {}
            override fun onRangeChangeListener(rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String?, rightPinValue: String?) {
                val valueLeft = leftPinValue.toString() + "0000"  ; val displayValueLeft = Utils.addWhiteSpace(valueLeft)
                val valueRight = rightPinValue.toString() + "0000"  ; val displayValueRight = Utils.addWhiteSpace(valueRight)
                a_search_tx_pricemini.text = displayValueLeft + "  $" ; a_search_tx_pricemax.text = displayValueRight + "  $"
                mPriceMini = valueLeft.toInt() ; mPriceMax = valueRight.toInt()
                if (mPriceMini == 100000 && mPriceMax == 3000000)
                {  mPriceMini = -1 ; mPriceMax = 999999999 }
                initSearch()} }) }

    private fun initPhoto() {
        a_search_ed_photo_rangebar.setOnRangeBarChangeListener(object :
            RangeBar.OnRangeBarChangeListener {
            override fun onTouchEnded(rangeBar: RangeBar?) {}
            override fun onRangeChangeListener(rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String?, rightPinValue: String?
            ) { a_search_ed_photo_mini.text = leftPinValue.toString(); a_search_ed_photo_max.text = rightPinValue.toString()
                mPhotoMini = leftPinValue!!.toInt() ; mPhotoMax = rightPinValue!!.toInt()
                if (mPhotoMini == 0 && mPhotoMax == 19)
                {  mPhotoMini = -1 ; mPhotoMax = 99 }
                initSearch()}

            override fun onTouchStarted(rangeBar: RangeBar?) {} }) }

    private fun initMarket() { a_search_cb_market.setOnCheckedChangeListener { _, b ->
        if (b){ mMarket = "ok" ;  initSearch()}
        else { mMarket = "%" ;  initSearch()} }}

    private fun initSchool() { a_search_cb_school.setOnCheckedChangeListener { _, b ->
        if (b){ mMarket = "ok" ;  initSearch()}
        else { mMarket = "%" ;  initSearch()} }}

    private fun initPharmacy() { a_search_cb_pharmacy.setOnCheckedChangeListener { _, b ->
        if (b){ mMarket = "ok" ;  initSearch()}
        else { mMarket = "%" ;  initSearch()} }}

    private fun initPark() { a_search_cb_park.setOnCheckedChangeListener { _, b ->
        if (b){ mMarket = "ok" ;  initSearch()}
        else { mMarket = "%" ;  initSearch()} }}

    private fun initDateSold() {
        a_search_sold_dateBegin.setOnClickListener {
            mSoldDateBegin = 1
            val dpd = OnDateSetListener { _, y, m, d -> a_search_sold_dateBegin.text = Utils.formatDate(y,m,d)
                mSoldDateBegin = Utils.convertToEpoch(Utils.formatDate(y,m,d))
                initSearch()}
            val now = Time()
            now.setToNow()
            val datePicker = DatePickerDialog(this, R.style.MyAppThemeCalendar, dpd, now.year, now.month, now.monthDay)
            datePicker.show()
            datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(resources.getColor(R.color.colorD))
            datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(resources.getColor(R.color.colorD))
            cancelDSB.visibility = View.VISIBLE
        }

        cancelDSB.setOnClickListener {
            println("-------C ------- DEC")
            a_search_sold_dateBegin.text = "Date -"
            mSoldDateBegin = 1
            cancelDSB.visibility = View.GONE
            initSearch()
        }


        a_search_sold_dateEnd.setOnClickListener {
            mSoldDateEnd = Utils.formatDateV2(Date())
            val dpd = OnDateSetListener { _, y, m, d -> a_search_sold_dateEnd.text = Utils.formatDate(y,m,d)
                mSoldDateEnd = Utils.convertToEpoch(Utils.formatDate(y,m,d))
                initSearch()}
            val now = Time()
            now.setToNow()
            val datePicker = DatePickerDialog(this, R.style.MyAppThemeCalendar, dpd, now.year, now.month, now.monthDay)
            datePicker.show()
            datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(resources.getColor(R.color.colorD))
            datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(resources.getColor(R.color.colorD))
            cancelDSE.visibility = View.VISIBLE
        }
        cancelDSE.setOnClickListener {
            println("-------C ------- DBC")
            a_search_sold_dateEnd.text = "Date +"
            mSoldDateEnd = 88888888870000
            cancelDSE.visibility = View.GONE
            initSearch()
        }}

    private fun initDateCreate() {
        a_search_ed_dateBegin_create.setOnClickListener {
            val dpd = OnDateSetListener { _, y, m, d ->
                a_search_ed_dateBegin_create.text = Utils.formatDate(y, m, d)
                mCreateDateBegin = Utils.convertToEpoch(Utils.formatDate(y, m, d))
                initSearch()}
            val now = Time()
            now.setToNow()
            val datePicker2 = DatePickerDialog(this, R.style.MyAppThemeCalendar, dpd, now.year, now.month, now.monthDay)
            datePicker2.show()
            datePicker2.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(resources.getColor(R.color.colorD))
            datePicker2.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(resources.getColor(R.color.colorD))
            cancelDCB.visibility = View.VISIBLE
        }
        cancelDCB.setOnClickListener {
            a_search_ed_dateBegin_create.text = "Date -"
            mCreateDateBegin = 1
            cancelDCB.visibility = View.GONE
            initSearch()
        }


        a_search_ed_dateEnd_create.setOnClickListener {
            mCreateDateEnd = Utils.formatDateV2(Date())
            val dpd = OnDateSetListener { _, y, m, d ->
                a_search_ed_dateEnd_create.text = Utils.formatDate(y,m,d)
                mCreateDateEnd = Utils.convertToEpoch(Utils.formatDate(y,m,d))
                initSearch()}
            val now = Time()
            now.setToNow()
            val datePicker = DatePickerDialog(this, R.style.MyAppThemeCalendar, dpd, now.year, now.month, now.monthDay)
            datePicker.show()
            datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(resources.getColor(R.color.colorD))
            datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(resources.getColor(R.color.colorD))
            cancelDCE.visibility = View.VISIBLE
        }
        cancelDCE.setOnClickListener {
            a_search_ed_dateEnd_create.text = "Date +"
            mCreateDateEnd = 88888888870000
            cancelDCE.visibility = View.GONE
            initSearch()}
    }



    private fun onClickHome() {
        a_search_fb_home.setOnClickListener { val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) } }

    private fun initSearch() {
        mMyViewModel.getSearchAll(
            mPerson, mType,
            mSurfaceMini, mSurfaceMax,
            mPriceMini, mPriceMax,
            mRoomMini, mRoomMax,
            mCreateDateBegin, mCreateDateEnd,
            mPhotoMini, mPhotoMax,
            mSoldDateBegin, mSoldDateEnd,
            mStatus,
            mPharmacy, mSchool, mMarket, mPark).observe(this, androidx.lifecycle.Observer {searchList ->


            var mListId = arrayListOf<Int>()
            searchList.forEach { mListId.add(it) }

            var mSearchlist =  listsEqual(mListId, mListIdForAdress)
            a_search_txt_item.text = mSearchlist.size.toString()

            clickSearch.setOnClickListener { loadRV(mSearchlist)}


        })}

    fun listsEqual(mListId: ArrayList<Int>, mListIdForAdress: ArrayList<Int>): ArrayList<Int> {
        val mDifference = mListId.minus(mListIdForAdress)
        mDifference.forEach { mListId.remove(it) }

        return mListId
    }

    private fun loadRV(mSearchlist: ArrayList<Int>) {


        if (mSearchlist.isNullOrEmpty()) {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent) }
        else { val intent = Intent(this, MainActivity::class.java)
            mSearchlist.sort()
            intent.putExtra(MASTER_ID, mSearchlist)
            println(" ------List Id------" + mSearchlist.toString())
            startActivity(intent) } }


    companion object {
        const val MASTER_ID = "master_id"
    }


    private fun testlook() {
        println (" -->mPerson<----" + mPerson)
        println (" -->mType <----" + mType)
        println (" -->mSurfac - <----" + mSurfaceMini)
        println (" -->mSurfac + <----" + mSurfaceMax)
        println (" -->mPriceM - <----" + mPriceMini)
        println (" -->mPriceM + <----" + mPriceMax)
        println (" -->mRoomMi - <----" + mRoomMini)
        println (" -->mRoomMa + <----" + mRoomMax)
        println (" -->mCreate - <----" + mCreateDateBegin)
        println (" -->mCreate + <----" + mCreateDateEnd)
        println (" -->mPhotoM - <----" + mPhotoMini)
        println (" -->mPhotoM + <----" + mPhotoMax)
        println (" -->mSoldDa - <----" + mSoldDateBegin)
        println (" -->mSoldDa + <----" + mSoldDateEnd)
        println (" -->mStatus<----" + mStatus)
        println (" -->mPharma<----" + mPharmacy)
        println (" -->mSchool<----" + mSchool)
        println (" -->mMarket<----" + mMarket)
        println (" -->mPark<----" + mPark)

        mMyViewModel.mAllEstate.observe(this, androidx.lifecycle.Observer {
            println(" ----Estate ------" + it.toString())
        })
    }
}
