package com.sofianem.realestatemanager.controller.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.sofianem.realestatemanager.R
import com.sofianem.realestatemanager.controller.adapter.PlacesListAdapter
import com.sofianem.realestatemanager.controller.fragment.DetailFragment
import com.sofianem.realestatemanager.data.Model.NearbyPlaces
import com.sofianem.realestatemanager.utils.Utils
import com.sofianem.realestatemanager.viewmodel.MyViewModel
import kotlinx.android.synthetic.main.activity_detail_map.*
import kotlin.math.roundToInt

class PlacesActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private var mCurrentLocation: String = ""
    var mId: Int = 0
    var mNewList = arrayListOf<String>()
    private var mNewNewList = arrayListOf<String>()
    private lateinit var mMyViewModel: MyViewModel
    private val mMarkerOptions = MarkerOptions()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_map)
        mCurrentLocation = intent.getStringExtra(DetailFragment.LOCATION)
        mId = intent.getIntExtra(DetailFragment.NEWID, 1)
        mMyViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        initfbPharmacy()
        initfbSchool()
        initfbMarket()
        initfbPark()
        onClickHome()

        loadMap() }

    private fun initfbPark() {
        fb_park_float.setOnClickListener {
            testOne("park", R.mipmap.flaggreen ) } }

    private fun initfbMarket() {
        fb_market_float.setOnClickListener {
            testOne("supermarket", R.mipmap.flagred ) } }

    private fun initfbSchool() {
        fb_school_float.setOnClickListener {
            testOne("primary_school", R.mipmap.flagorange ) } }

    private fun initfbPharmacy() {
        fb_pharmacy_float.setOnClickListener {
            testOne("pharmacy", R.mipmap.flagblue ) } }



    private fun testOne(mType1: String, icon: Int) {

        mMyViewModel.getByIdLocation(mType1, mId).observe(this, Observer { listNearbyPlaces ->
            mMap?.clear()
            listNearbyPlaces?.forEach { np ->
                println("-------listNearbyPlaces------------" + np.toString() )
                val location = Utils.formatLatLng(np.placeLocation)
                val markerO = MarkerOptions()
                markerO.icon(BitmapDescriptorFactory.fromResource(icon))
                markerO.position(location).title(np.placeName)
                mMap?.addMarker(markerO) }
            mMap?.addMarker(mMarkerOptions)
            loadRV(listNearbyPlaces, mType1) }) }

    private fun loadRV(it: List<NearbyPlaces>?, mType1: String) {
        mNewList.clear()
        mNewNewList.clear()
        it?.forEach { np ->
            val latPlace = Utils.latitude(np.placeLocation)
            val lngPlace = Utils.longitude(np.placeLocation)
            val currLat = Utils.latitude(mCurrentLocation);
            val currLng = Utils.longitude(mCurrentLocation)
            val distance = Utils.calculateDistance(currLat, currLng, latPlace, lngPlace).roundToInt()

            mNewList.add(mNewList.size, np.placeName)
            mNewNewList.add(mNewNewList.size, distance.toString()) }
            activity_detail_map_RVok.layoutManager = GridLayoutManager(this, 3)
                activity_detail_map_RVok.adapter = PlacesListAdapter(mNewList, mNewNewList, mType1) }

    fun onClickHome() {
        detail_map_fb_home.setOnClickListener { val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()} }

    private fun loadHouse() {
        val mLatLng = Utils.formatLatLng(mCurrentLocation)
        mMarkerOptions.position(mLatLng).title("House").icon(BitmapDescriptorFactory.fromResource(R.mipmap.flaggrey))
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15.0f))
        mMap?.addMarker(mMarkerOptions) }
    
    private fun loadMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.activity_detail_map_view) as SupportMapFragment
        mapFragment.getMapAsync(this) }

    override fun onMapReady(map: GoogleMap?) {
        this.mMap = map
        loadHouse() }
}