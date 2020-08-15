package com.sofianem.realestatemanager.controller.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProviders
import com.sofianem.realestatemanager.R
import com.sofianem.realestatemanager.controller.fragment.DetailFragment
import com.sofianem.realestatemanager.controller.fragment.MainFragment
import com.sofianem.realestatemanager.data.model.EstateR
import com.sofianem.realestatemanager.utils.MyCommunication
import com.sofianem.realestatemanager.viewmodel.MyViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MyCommunication, LifecycleObserver {

    private var mIsDualPane = true
    private var mListId: ArrayList<Int>? = arrayListOf()
    private lateinit var mMyViewModel: MyViewModel
    private var mListData: List<EstateR>? = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragmentDetailView = findViewById<View>(R.id.fragment_main_detail)
        setContentView(R.layout.activity_main)
        mIsDualPane = fragmentDetailView?.visibility == View.VISIBLE
        onClickCreate()
        onClickMap()
        onClickSearch()
        onClickCAl()
        mListId = intent.getIntegerArrayListExtra("masterId")
        val fragment = MainFragment.newInstance(mListId)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentMain, fragment).commit()
        supportFragmentManager.executePendingTransactions()
        mMyViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)
        //  test(id)
    }

    private fun onClickSearch() {
        activity_main_floating_search.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent) } }


    private fun onClickCreate() {
        activity_main_floating_add.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent) } }

    private fun onClickMap() {
        activity_main_floating_maps.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent) } }

    private fun onClickCAl() {
        activity_main_floating_cal.setOnClickListener {
            val intent = Intent(this, CalculatorActivity::class.java)
            startActivity(intent) } }



    override fun displayDetails(id: Int) {
        if (mIsDualPane) {
            println("--------------------Tablet--------------")
            val fragmentMainDetail = supportFragmentManager.findFragmentById(R.id.fragment_main_detail) as DetailFragment?
            fragmentMainDetail?.displayDetails(id)
            test()
            activity_main_floating_update.visibility = View.VISIBLE
            activity_main_floating_update.setOnClickListener {
                val intent = Intent(this, UpdateActivity::class.java)
                intent.putExtra(ID, id)
                startActivity(intent) } }

        else  {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(ID, id)
            startActivity(intent)
            test()}
    }

    private fun test() {

        println(" mListData --------" + mListData)
    }
    /*
        mMyViewModel.retrieveData().observe(this, Observer {
            mListData = it
            subscriber_recyclerView.adapter?.notifyDataSetChanged()
        })
        mListData?.forEach { estate ->
            if (estate.prox_school == "Estate_school" && estate.prox_park == "Estate_park" && estate.prox_market == "Estate_market" && estate.prox_pharmacy == "Estate_pharmacy") {
                println(" GO ----------->" + estate.id + "  " + estate.location + "   " + estate.adress)
                savePlace(estate.id, estate.location)
            } else {
                println(" SAVED ----------->"
                        + estate.id + " "
                        + estate.prox_pharmacy
                        + "  " + estate.prox_park + " "
                        + estate.prox_market + "  "
                        + estate.prox_school )
            }
        }
    }
     */

    companion object { const val ID = "id" }

}
