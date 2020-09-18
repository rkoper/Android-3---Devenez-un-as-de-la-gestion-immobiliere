package com.sofianem.realestatemanager.controller.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.sofianem.realestatemanager.R
import com.sofianem.realestatemanager.controller.activity.MainActivity.Companion.ID
import com.sofianem.realestatemanager.controller.fragment.DetailFragment
import com.sofianem.realestatemanager.viewmodel.MyViewModel
import com.sofianem.realestatemanager.viewmodel.MyViewModelForImages
import com.sofianem.realestatemanager.viewmodel.MyViewModelForPlaces
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.android.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private val mMyViewModel by viewModel<MyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val id = intent.getIntExtra(ID, 1)
        mMyViewModel.mAllEstate.observe(this, Observer { detail_fb_edit.isVisible = it[id-1].status == "ok" })
        initFrag(id)
        onclickUpdate(id)
        onClickHome()
        onClickMap()
        onClickSearch()
        onClickCAl() }

    private fun initFrag(id: Int) {
        val fragmentMainDetail = supportFragmentManager.findFragmentById(R.id.fragment_main_detail) as DetailFragment?
        fragmentMainDetail?.displayDetails(id) }

    private fun onClickSearch() {
        detail_fb_search.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent) } }


    private fun onClickMap() {
        detail_fb_map.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent) } }

    private fun onClickCAl() {
        detail_fb_cal.setOnClickListener {
            val intent = Intent(this, CalculatorActivity::class.java)
            startActivity(intent) } }

    private fun onclickUpdate(id: Int) {
        detail_fb_edit.setOnClickListener {
            val intent = Intent(this, UpdateActivity::class.java)
            intent.putExtra(ID, id)
            startActivity(intent) } }


    private fun onClickHome() {
        detail_fb_home.setOnClickListener { val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()} }



}
