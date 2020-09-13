package com.sofianem.realestatemanager.controller.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.sofianem.realestatemanager.utils.Utils
import com.sofianem.realestatemanager.viewmodel.MyViewModel
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mLastLocation: Location
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mLocationCallback: LocationCallback
    private lateinit var mMyViewModel: MyViewModel
    private var mLatitude: Double = 0.toDouble()
    private var mLongitude: Double = 0.toDouble()
    private var mMarker: Marker? = null
    private var mItemMarker: Marker? = null
    private var mPermissions: Array<String> = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.sofianem.realestatemanager.R.layout.activity_map)
        mMyViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)
        loadMap()
        allMarker()
        onClick() }

    private fun onClick() {
        activity_map_floating.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) } }

    private fun allMarker() {

        mMyViewModel.allWordsLive.observe(this, Observer {
            it.forEach { estate ->
                val location = Utils.formatLatLng(estate.location)
                val markerOptions = MarkerOptions()
                markerOptions.position(location)
                markerOptions.snippet(estate.id.toString())
                println("---------   1   estate.id    ------->" + estate.id)
                markerOptions.icon(BitmapDescriptorFactory.fromResource(com.sofianem.realestatemanager.R.drawable.home))
                mItemMarker = mMap.addMarker(markerOptions)
                mMap.setOnMarkerClickListener { m -> onMarkerClick(m) } } })


                }



    private fun onMarkerClick(m: Marker): Boolean {
        val mId: Int = mItemMarker!!.snippet.toInt()
        println("---------   2   mId    ------->" + mId)
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("id", mId)
        startActivity(intent)
        return true }


    private fun loadMap() {
        val mapFragment = supportFragmentManager.findFragmentById(com.sofianem.realestatemanager.R.id.mapViewMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                buildLocationRequest()
                buildLocationCallback()
                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper()) }
            else {
                buildLocationRequest()
                buildLocationCallback()
                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper()) } } }



    private fun checkLocationPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
                ActivityCompat.requestPermissions(this, mPermissions, PERMISSION_REQUEST)
            else ActivityCompat.requestPermissions(this, mPermissions, PERMISSION_REQUEST)
            false
        } else true
    }

    override fun onRequestPermissionsResult(requestCode: Int, mPermissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    if (Utils.askForLoc(this) == PackageManager.PERMISSION_GRANTED)
                        if (checkLocationPermission()) {
                            buildLocationRequest()
                            buildLocationCallback()
                            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
                            mMap.isMyLocationEnabled = true } }
                else { Toast.makeText(this, "Permission is denied", Toast.LENGTH_SHORT).show() } } } }


    override fun onStop() { mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
        super.onStop() }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mMap != null) { val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                if (permission == PackageManager.PERMISSION_GRANTED) { mMap.isMyLocationEnabled = true
                    uiSettings()} }

            else { requestPermissions(mPermissions, PERMISSION_REQUEST)
                mMap.isMyLocationEnabled = true
                uiSettings()} } }

    private fun uiSettings() {
        mMap.uiSettings.isTiltGesturesEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true }

    private fun buildLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.smallestDisplacement = 10f }

    private fun buildLocationCallback() {
        mLocationCallback = object : LocationCallback() { override fun onLocationResult(p0: LocationResult?) {
            mLastLocation = p0!!.locations[p0.locations.size - 1]
            if (mMarker != null) { mMarker!!.remove() }
            mLatitude = mLastLocation.latitude
            mLongitude = mLastLocation.longitude

            val latlng = LatLng(mLatitude, mLongitude)
            val markerOptions = MarkerOptions().position(latlng).title("My Position")
                .icon(BitmapDescriptorFactory.fromResource(com.sofianem.realestatemanager.R.drawable.target))
            mMarker = mMap.addMarker(markerOptions)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15.0f)) } } }

    companion object { private const val PERMISSION_REQUEST: Int = 1000 }

}
