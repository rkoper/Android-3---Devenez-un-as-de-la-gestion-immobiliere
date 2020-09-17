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
    private val mMyViewModel by viewModel<MyViewModel>()
    private val mMyViewModelForImages by viewModel<MyViewModelForImages>()
    private val mMyViewModelForPlaces by viewModel<MyViewModelForPlaces>()
    private lateinit var mEstate: List<EstateR>
    private lateinit var mPlace: List<NearbyPlaces>
    private var mId: Int = 0
    private val mListImagePath: MutableList<String?> = ArrayList()
    private val mListImageDescription: MutableList<String?> = ArrayList()
    var mLocationForPlace = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { return inflater.inflate(R.layout.fragment_detail, container, false) }

    fun displayDetails(id: Int) {
            mMyViewModel.allWordsLive.observe(this, Observer {
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                mId = id - 1
                initType(mId, it)
                initPrice(mId, it)
                initCity(mId, it)
                initSurface(mId, it)
                initRoom(mId, it)
                initDescription(mId, it)
                initAdress(mId, it)
                initPersonn(mId, it)
                initDate(mId, it, sdf)
                initStatus(mId, it)
                initLocation(mId, it)
                initProxLoc(id, it) })
        setupRecyclerView(id) }

    private fun initProxLoc(mId: Int, it: List<EstateR>?) {
        mMyViewModelForPlaces.getByIdLocation("park", mId).observe(this, Observer { lnp ->
            detail_park_txt.text = lnp[0].placeDistance.toString() + " m"
            println("-----------" +lnp[0].placeDistance )
     }).also {
            mMyViewModelForPlaces.getByIdLocation("supermarket", mId).observe(this, Observer { lnp ->
                detail_market_txt.text = lnp[0].placeDistance.toString() + " m "
                println("-----------" +lnp[0].placeDistance )
        })}.also {
            mMyViewModelForPlaces.getByIdLocation("primary_school", mId).observe(this, Observer { lnp ->
                detail_school_txt.text = lnp[0].placeDistance.toString()+ " m "
                println("-----------" +lnp[0].placeDistance )
        })}.also {
            mMyViewModelForPlaces.getByIdLocation("pharmacy", mId).observe(this, Observer { lnp ->
                detail_pharmacy_txt.text = lnp[0].placeDistance.toString()+ " m "
            println("-----------" +lnp[0].placeDistance ) }) }}



    private fun setupRecyclerView(mId: Int) {
        mListImageDescription.clear()
        mListImagePath.clear()
        mMyViewModelForImages.allImageLive.observe(this, Observer { listImage ->
            if (listImage.isNullOrEmpty()) { Toast.makeText(requireContext(), "xxx", Toast.LENGTH_SHORT).show() }
            else { listImage.forEach { value ->
                if (value.masterId == mId) { mListImagePath.add(value.imageUri)
                    mListImageDescription.add(value.imageDescription) } }
                detail_recyclerview.adapter = DetailAdapter(mListImagePath, mListImageDescription, requireContext()) } })

        val layoutManager = GridLayoutManager(requireContext(), 3)
        detail_recyclerview.layoutManager = layoutManager }


    private fun initLocation(mId: Int, it: List<EstateR>?) {
        if (it?.get(mId)!!.location != "null") {
            mLocationForPlace = it[mId].location
            val locationToDisplay = "https://maps.googleapis.com/maps/api/staticmap?center=$mLocationForPlace&zoom=20&size=2400x1200&maptype=roadmap&markers=color:red%7Clabel:S%7C$mLocationForPlace&key=AIzaSyC-Hromy2t2Pfgd-qlYnDk0SOVdVmctrvc"
            Glide.with(this).load(locationToDisplay).into(detail_map)
            detail_map.setOnClickListener {
                val intent = Intent(activity, PlacesActivity::class.java)
                intent.putExtra(NEWID, mId + 1)
                intent.putExtra(LOCATION, mLocationForPlace)
                activity?.startActivity(intent) } } }

    private fun initStatus(mId: Int, it: List<EstateR>?) {
        if (it?.get(mId)!!.status == "sold") { detail_sold.isVisible = true
            detail_sold.setOnClickListener { Toast.makeText(requireContext(), "Already sold", Toast.LENGTH_LONG).show() }
            cancel_sold.setOnClickListener {
                val intent = Intent(activity, UpdateActivity::class.java)
                intent.putExtra(ID, mId)
                startActivity(intent) } } }

    private fun initDate(mId: Int, it: List<EstateR>, sdf: SimpleDateFormat) {
        if (it[mId].date_begin.toInt() == 3) { detail_datebegin.text = "-"} else{
            val mDisplayDateBegin = sdf.format(Date(it[mId].date_begin))
            detail_datebegin.text = mDisplayDateBegin.toString() }

        if (it[mId].date_end == 8888888888) { detail_dateend.text = "-" }  else {
            val mDisplayDateEnd = sdf.format(Date(it[mId].date_end))
            detail_dateend.text = mDisplayDateEnd.toString() } }

    private fun initPersonn(mId: Int, it: List<EstateR>?) {
        if (it?.get(mId)!!.personn == "") {   detail_personn.text   = "-"}
        else { detail_personn.text = it[mId].personn}}

    private fun initAdress(mId: Int, it: List<EstateR>?) { detail_adress.text = it?.get(mId)!!.adress }

    private fun initDescription(mId: Int, it: List<EstateR>?) {

        if (it?.get(mId)!!.description == "") {
            detail_description.text = "     -     "
        } else {
            detail_description.text = it[mId].description
            detail_description.setOnClickListener { _ ->
                val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_description_detail, null)
                mDialogView.requestFocus()
                val builder = AlertDialog.Builder(this!!.context!!).setView(mDialogView)
                val q: TextView = mDialogView.findViewById(R.id.dialog_imageview_text)
                q.text = it[mId].description
                builder.show()
            } } }

    private fun initRoom(mId: Int, it: List<EstateR>?) { if (it!![mId].number_of_room == 0) {   detail_room.text = "-"}
    else { detail_room.text = it[mId].number_of_room.toString() } }

    private fun initCity(mId: Int, it: List<EstateR>?) { detail_city.text = it!![mId].city }

    private fun initPrice(mId: Int, itPrice: List<EstateR>?) {
        if (itPrice!![mId].price == 0) {
            detail_tx_pric.text = "   -   "
            detail_tx_pric_dollar.setTextColor(resources.getColor(R.color.colorPaleBlue))
            detail_tx_pric_euro.setTextColor(resources.getColor(R.color.colorPaleBlue))
            detail_tx_pric_dollar.isClickable = false
            detail_tx_pric_euro.isClickable = false }

        else {
            loadPriceDollar(itPrice)
            detail_tx_pric_euro.setOnClickListener { o ->
                val value = Utils.convertDollarToEuro(itPrice[mId].price)
                val displayValue = Utils.addWhiteSpace(value.toString())
                detail_tx_pric.text = "$displayValue  €"
                detail_tx_pric_dollar.setTextColor(resources.getColor(R.color.colorPaleBlue))
                detail_tx_pric_euro.setTextColor(resources.getColor(R.color.colorD))
                detail_tx_pric_dollar.isClickable = true
                detail_tx_pric_euro.isClickable = false}

            detail_tx_pric_dollar.setOnClickListener { o ->
                loadPriceDollar(itPrice) } }
    }

    private fun loadPriceDollar(itPrice: List<EstateR>) {
        val displayValue = Utils.addWhiteSpace(itPrice[mId].price.toString())
        detail_tx_pric.text = "$displayValue  $"
        detail_tx_pric_dollar.setTextColor(resources.getColor(R.color.colorD))
        detail_tx_pric_dollar.isClickable = false
        detail_tx_pric_euro.isClickable = true
        detail_tx_pric_euro.setTextColor(resources.getColor(R.color.colorPaleBlue))
    }


    private fun initSurface(mId: Int, itSurface: List<EstateR>?) {
        if (itSurface!![mId].surface == 0) {
            detail_tx_surface.text = "         -         "
            detail_tx_surface_square.setTextColor(resources.getColor(R.color.colorPaleBlue))
            detail_tx_surface_m2.setTextColor(resources.getColor(R.color.colorPaleBlue))
            detail_tx_surface_square.isClickable = false
            detail_tx_surface_m2.isClickable = false }
        else {
            loadSurfaceSq(itSurface)
            detail_tx_surface_m2.setOnClickListener {
                val value = Utils.convertSqTom2(itSurface[mId].surface)
                detail_tx_surface.text = "$value     m²"
                detail_tx_surface_square.setTextColor(resources.getColor(R.color.colorPaleBlue))
                detail_tx_surface_m2.setTextColor(resources.getColor(R.color.colorD))
                detail_tx_surface_square.isClickable = true
                detail_tx_surface_m2.isClickable = false}

            detail_tx_surface_square.setOnClickListener { loadSurfaceSq(itSurface) } }
    }

    private fun loadSurfaceSq(itSurface: List<EstateR>) {
        detail_tx_surface.text = itSurface[mId].surface.toString() + "    Sq/ft"
        detail_tx_surface_square.setTextColor(resources.getColor(R.color.colorD))
        detail_tx_surface_square.isClickable = false
        detail_tx_surface_m2.isClickable = true
        detail_tx_surface_m2.setTextColor(resources.getColor(R.color.colorPaleBlue))
    }

    private fun initType(mId: Int, it: List<EstateR>?) {
        if (it?.get(mId)!!.type == "") {   detail_type.text  = "-"}
        else { detail_type.text = it[mId].type} }

    companion object {
        const val NEWID = "newId"
        const val LOCATION = "Location" }

}
