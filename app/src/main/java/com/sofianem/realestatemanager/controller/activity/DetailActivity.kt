package com.sofianem.realestatemanager.controller.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import com.sofianem.realestatemanager.R
import com.sofianem.realestatemanager.controller.activity.MainActivity.Companion.ID
import com.sofianem.realestatemanager.controller.fragment.DetailFragment
import com.sofianem.realestatemanager.viewmodel.MyViewModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var mMyViewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val id = intent.getIntExtra(ID, 1)
        val fragmentMainDetail = supportFragmentManager.findFragmentById(R.id.fragment_main_detail) as DetailFragment?
        fragmentMainDetail?.displayDetails(id)

        mMyViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)
        mMyViewModel.retrieveData().observe(this, androidx.lifecycle.Observer { a_detail_fb_update.isVisible = it[id-1].status == "ok" })

        onclickUpdate(id)
        onClickHome()
    }

    private fun onclickUpdate(id: Int) {
        a_detail_fb_update.setOnClickListener {
            val intent = Intent(this, UpdateActivity::class.java)
            intent.putExtra(ID, id)
            startActivity(intent) } }


    private fun onClickHome() {
        detail_fb_home.setOnClickListener { val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()} }



}
