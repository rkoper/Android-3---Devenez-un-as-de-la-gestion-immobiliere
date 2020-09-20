package com.sofianem.realestatemanager.controller.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import com.sofianem.realestatemanager.R
import com.sofianem.realestatemanager.controller.fragment.DetailFragment
import com.sofianem.realestatemanager.controller.fragment.MainFragment
import com.sofianem.realestatemanager.utils.MyCommunication
import com.sofianem.realestatemanager.viewmodel.MyViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), MyCommunication, LifecycleObserver {
    var mIsDualPane = false
    private var mSearchlist: ArrayList<Int>? = arrayListOf()
    private val mMyViewModel by viewModel<MyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         var fragmentDetailView = findViewById<View>(R.id.fragment_main_detail)
        mIsDualPane = !(fragmentDetailView == null || !fragmentDetailView.isVisible)
        mSearchlist = intent.getIntegerArrayListExtra("master_id")

        println(" ------List Id------" + mSearchlist.toString())
        val fragment = MainFragment.newInstance(mSearchlist)

        if (!mSearchlist.isNullOrEmpty())
        { onClickRefresh((mSearchlist!!))}

        onClickAdd()
        onClickMap()
        onClickSearch()
        onClickCAl()


        // testtest()

        supportFragmentManager.beginTransaction().replace(R.id.fragmentMain, fragment).commit()
        supportFragmentManager.executePendingTransactions()
    }

    private fun onClickRefresh(mSearchlist: java.util.ArrayList<Int>) {
        if (!mSearchlist.isNullOrEmpty()) {
            activity_main_floating_refresh.visibility = View.VISIBLE
            activity_main_floating_refresh.setOnClickListener {
                mSearchlist.clear()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }



        }
    }

    private fun testtest() {
        mMyViewModel.mAllEstate.observe(this, Observer {
            println(" -------- ALL DATA -----" + it.toString())
        })
    }

    private fun onClickSearch() {
        activity_main_floating_search.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent) } }


    private fun onClickAdd() {
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
            cacheForDetail.isVisible = false
            val fragmentMainDetail = supportFragmentManager.findFragmentById(R.id.fragment_main_detail) as DetailFragment?
            fragmentMainDetail?.displayDetails(id)
            activity_main_floating_update.isVisible = true
            activity_main_floating_update.setOnClickListener {
                val intent = Intent(this, UpdateActivity::class.java)
                intent.putExtra(ID, id)
                startActivity(intent) } }

        else  {
            println("--------------------phone--------------")
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(ID, id)
            startActivity(intent)
            finish()} }

    companion object {
        const val ID = "id" }
}
