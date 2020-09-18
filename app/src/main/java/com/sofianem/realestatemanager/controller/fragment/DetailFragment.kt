package com.sofianem.realestatemanager.controller.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.sofianem.realestatemanager.R
import com.sofianem.realestatemanager.controller.activity.MainActivity.Companion.ID
import com.sofianem.realestatemanager.controller.activity.PlacesActivity
import com.sofianem.realestatemanager.controller.activity.UpdateActivity
import com.sofianem.realestatemanager.controller.adapter.DetailAdapter
import com.sofianem.realestatemanager.data.model.EstateR
import com.sofianem.realestatemanager.data.model.NearbyPlaces
import com.sofianem.realestatemanager.utils.Utils
import com.sofianem.realestatemanager.viewmodel.MyViewModel
import com.sofianem.realestatemanager.viewmodel.MyViewModelForImages
import com.sofianem.realestatemanager.viewmodel.MyViewModelForPlaces
import kotlinx.android.synthetic.main.fragment_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class DetailFragment : Fragment(), LifecycleObserver {
    private val mMV by viewModel<MyViewModel>()
    private val mMVImage by viewModel<MyViewModelForImages>()
    private val mMVForPlaces by viewModel<MyViewModelForPlaces>()
    private lateinit var mEstate: List<EstateR>
    private lateinit var mPlace: List<NearbyPlaces>
    private var mId: Int = 0
    private lateinit var lnp : List<NearbyPlaces>

    private val mListImagePath: MutableList<String?> = ArrayList()
    private val mListImageDescription: MutableList<String?> = ArrayList()
    var mLocationForPlace = ""
    var mType: String = ""
    var mCity: String = ""
    var mDescription: String = ""
    var mAddress: String = ""
    var mStatus: String = ""
    var mGeoLoc: String = ""
    var mPerson: String = ""
    var mPrice: Int = 0
    var mSurface: Int = 0
    var mNumberOfRoom: Int = 0
    var mDateBegin: Long = 3
    var mDateEnd: Long = 8888888888
    var mProxPark: String = ""
    var mProxSchool: String = ""
    var mProxMarket: String = ""
    var mProxPharmacy: String = ""
    var mCreateId: Int = 99
    var mNbPhoto:Int = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { return inflater.inflate(R.layout.fragment_detail, container, false) }

    fun displayDetails(id: Int) {


       mMV.getById(id).observe(this, Observer {mEstate ->
           println( " T1 ----------> + " + mEstate )
           mId = mEstate.id
           mType = mEstate.type
           mPrice = mEstate.price
           mCity = mEstate.city
           mSurface = mEstate.surface
           mNumberOfRoom = mEstate.number_of_room
           mDescription = mEstate.description
           mAddress = mEstate.adress
           mPerson = mEstate.personn
           mDateBegin = mEstate.date_begin
           mStatus = mEstate.status
           mLocationForPlace = mEstate.location
           mProxPark = mEstate.prox_park
           mProxSchool= mEstate.prox_school
           mProxMarket = mEstate.prox_market
           mProxPharmacy = mEstate.prox_pharmacy
           initType()
           initPrice()
           initCity()
           initSurface()
           initRoom()
           initDescription()
           initAdress()
           initPersonn()
           initDate()
           initStatus()
           initLocation()
           initProxLoc()
           setupRecyclerView()})

       }

    private fun initProxLoc() {
        mMVForPlaces.getByIdLocation("park", mId).observe(this, Observer { lnp ->
            detail_park_txt.text = lnp[0].placeDistance.toString() + " m "
            if (mProxPark == "Estate_park"){
                if ( lnp[0].placeDistance < 500) {mMV.UpdateProxPark("ok", mId )}
                else {mMV.UpdateProxPark("no", mId )}}
        }).also {
            mMVForPlaces.getByIdLocation("supermarket", mId).observe(this, Observer { lnp ->
                detail_market_txt.text = lnp[0].placeDistance.toString() + " m "
                if (mProxMarket == "Estate_market"){
                   if ( lnp[0].placeDistance < 500) {mMV.UpdateProxMarket("ok", mId )}
                   else {mMV.UpdateProxMarket("no", mId )}}
        })}.also {
            mMVForPlaces.getByIdLocation("primary_school", mId).observe(this, Observer { lnp ->
                detail_school_txt.text = lnp[0].placeDistance.toString()+ " m "
                if (mProxSchool == "Estate_school"){
                    if ( lnp[0].placeDistance < 500) {mMV.UpdateProxSchool("ok", mId )}
                    else {mMV.UpdateProxSchool("no", mId )}}
        })}.also {
            mMVForPlaces.getByIdLocation("pharmacy", mId).observe(this, Observer { lnp ->
                detail_pharmacy_txt.text = lnp[0].placeDistance.toString()+ " m "
                if (mProxPharmacy == "Estate_pharmacy"){
                    if ( lnp[0].placeDistance < 500) {mMV.UpdateProxPharma("ok", mId )}
                    else {mMV.UpdateProxPharma("no", mId )}} }) }}



    private fun setupRecyclerView() {
        mListImageDescription.clear()
        mListImagePath.clear()
        mMVImage.allImageLive.observe(this, Observer { listImage ->
            if (listImage.isNullOrEmpty()) { Toast.makeText(requireContext(), "No photo", Toast.LENGTH_SHORT).show() }
            else { listImage.forEach { img ->
                if (img.masterId == mId) { mListImagePath.add(img.imageUri)
                    mListImageDescription.add(img.imageDescription) }}
                detail_recyclerview.adapter = DetailAdapter(mListImagePath, mListImageDescription, requireContext()) } })

        val layoutManager = GridLayoutManager(requireContext(), 3)
        detail_recyclerview.layoutManager = layoutManager }


    private fun initLocation() {
        if (mLocationForPlace != "null") {
            val locationToDisplay = "https://maps.googleapis.com/maps/api/staticmap?center=$mLocationForPlace&zoom=20&size=2400x1200&maptype=roadmap&markers=color:red%7Clabel:S%7C$mLocationForPlace&key=AIzaSyC-Hromy2t2Pfgd-qlYnDk0SOVdVmctrvc"
            Glide.with(this).load(locationToDisplay).into(detail_map)
            detail_map.setOnClickListener {
                val intent = Intent(activity, PlacesActivity::class.java)
                intent.putExtra(NEWID, mId)
                intent.putExtra(LOCATION, mLocationForPlace)
                activity?.startActivity(intent) } } }

    private fun initStatus() {
        if (mStatus == "sold") { detail_sold.isVisible = true
            detail_sold.setOnClickListener { Toast.makeText(requireContext(), "Already sold", Toast.LENGTH_LONG).show() }
            cancel_sold.setOnClickListener {
                val intent = Intent(activity, UpdateActivity::class.java)
                intent.putExtra(ID, mId)
                startActivity(intent) } } }

    private fun initDate() {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        if (mDateBegin.toInt() == 3) { detail_datebegin.text = "-"} else{
            val mDisplayDateBegin = sdf.format(Date(mDateBegin))
            detail_datebegin.text = mDisplayDateBegin.toString() }

        if (mDateEnd == 8888888888) { detail_dateend.text = "-" }  else {
            val mDisplayDateEnd = sdf.format(Date(mDateEnd))
            detail_dateend.text = mDisplayDateEnd.toString() } }

    private fun initPersonn() {
        if (mPerson == "") {   detail_personn.text   = "-"}
        else { detail_personn.text = mPerson}}

    private fun initAdress() { detail_adress.text = mAddress }

    private fun initDescription() {

        if (mDescription == "") {
            detail_description.text = "     -     "
        } else {
            detail_description.text = mDescription
            detail_description.setOnClickListener { _ ->
                val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_description_detail, null)
                mDialogView.requestFocus()
                val builder = AlertDialog.Builder(this!!.requireContext()).setView(mDialogView)
                val q: TextView = mDialogView.findViewById(R.id.dialog_imageview_text)
                q.text = mDescription
                builder.show()
            } } }

    private fun initRoom() { if (mNumberOfRoom == 0) {   detail_room.text = "-"}
    else { detail_room.text = mNumberOfRoom.toString() } }

    private fun initCity() { detail_city.text = mCity }

    private fun initPrice() {
        if (mPrice == 0) {
            detail_tx_pric.text = "   -   "
            detail_tx_pric_dollar.setTextColor(resources.getColor(R.color.colorPaleBlue))
            detail_tx_pric_euro.setTextColor(resources.getColor(R.color.colorPaleBlue))
            detail_tx_pric_dollar.isClickable = false
            detail_tx_pric_euro.isClickable = false }

        else {
            loadPriceDollar()
            detail_tx_pric_euro.setOnClickListener { o ->
                val value = Utils.convertDollarToEuro(mPrice)
                val displayValue = Utils.addWhiteSpace(value.toString())
                detail_tx_pric.text = "$displayValue  €"
                detail_tx_pric_dollar.setTextColor(resources.getColor(R.color.colorPaleBlue))
                detail_tx_pric_euro.setTextColor(resources.getColor(R.color.colorD))
                detail_tx_pric_dollar.isClickable = true
                detail_tx_pric_euro.isClickable = false}

            detail_tx_pric_dollar.setOnClickListener { o ->
                loadPriceDollar() } }
    }

    private fun loadPriceDollar() {
        val displayValue = Utils.addWhiteSpace(mPrice.toString())
        detail_tx_pric.text = "$displayValue  $"
        detail_tx_pric_dollar.setTextColor(resources.getColor(R.color.colorD))
        detail_tx_pric_dollar.isClickable = false
        detail_tx_pric_euro.isClickable = true
        detail_tx_pric_euro.setTextColor(resources.getColor(R.color.colorPaleBlue))
    }


    private fun initSurface() {
        if (mSurface == 0) {
            detail_tx_surface.text = "         -         "
            detail_tx_surface_square.setTextColor(resources.getColor(R.color.colorPaleBlue))
            detail_tx_surface_m2.setTextColor(resources.getColor(R.color.colorPaleBlue))
            detail_tx_surface_square.isClickable = false
            detail_tx_surface_m2.isClickable = false }
        else {
            loadSurfaceSq()
            detail_tx_surface_m2.setOnClickListener {
                val value = Utils.convertSqTom2(mSurface)
                detail_tx_surface.text = "$value     m²"
                detail_tx_surface_square.setTextColor(resources.getColor(R.color.colorPaleBlue))
                detail_tx_surface_m2.setTextColor(resources.getColor(R.color.colorD))
                detail_tx_surface_square.isClickable = true
                detail_tx_surface_m2.isClickable = false}

            detail_tx_surface_square.setOnClickListener { loadSurfaceSq() } }
    }

    private fun loadSurfaceSq() {
        detail_tx_surface.text = mSurface.toString() + "    Sq/ft"
        detail_tx_surface_square.setTextColor(resources.getColor(R.color.colorD))
        detail_tx_surface_square.isClickable = false
        detail_tx_surface_m2.isClickable = true
        detail_tx_surface_m2.setTextColor(resources.getColor(R.color.colorPaleBlue))
    }

    private fun initType() {
        if (mType == "") {   detail_type.text  = "-"}
        else { detail_type.text = mType} }

    companion object {
        const val NEWID = "newId"
        const val LOCATION = "Location" }

}
