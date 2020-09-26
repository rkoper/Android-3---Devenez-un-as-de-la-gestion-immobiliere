package com.sofianem.realestatemanager.controller.activity

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.widget.RemoteViews
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
import kotlin.math.pow
import kotlin.math.sqrt

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), MyCommunication, LifecycleObserver {
    private var mIsDualPane = false
    private var mSearchlist: ArrayList<Int>? = arrayListOf()
    private val mMyViewModel by viewModel<MyViewModel>()
    private var mNewID : Int = 999999
    private lateinit var notificationManager : NotificationManager
    private lateinit var notificationChannel : NotificationChannel
    private lateinit var builder : Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentDetailView = findViewById<View>(R.id.fragment_main_detail)
        mIsDualPane = !(fragmentDetailView == null || !fragmentDetailView.isVisible)
        mSearchlist = intent.getIntegerArrayListExtra("master_id")
        mNewID = intent.getIntExtra("new_ID", 99999)

        println(" ----- create 2 -------->>>>>>")

        val fragment = MainFragment.newInstance(mSearchlist)

        if (!mSearchlist.isNullOrEmpty())
        { onClickRefresh((mSearchlist!!))}

        if (mNewID != 99999) { initNotif(mNewID)}

        onClickAdd()
        onClickMap()
        onClickSearch()
        onClickCAl()

        supportFragmentManager.beginTransaction().replace(R.id.fragmentMain, fragment).commit()
        supportFragmentManager.executePendingTransactions()
    }

    private fun initNotif(mNewID: Int) {
        val mCity = mMyViewModel.getForNotif(mNewID)

            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val intent = Intent(this,MainActivity::class.java)
            println(" Go notif------>>>>>>>>>>>>>>>")
            val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
            val contentView = RemoteViews(packageName,R.layout.activity_notification_view)
            contentView.setTextViewText(R.id.tv_title,"New item on Real Estate Manager")
            contentView.setTextViewText(R.id.tv_content, "@ $mCity")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.WHITE
                notificationChannel.enableVibration(true)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(this,channelId).setContent(contentView).setSmallIcon(R.mipmap.ic_launcher_rem_round).setLargeIcon(BitmapFactory.decodeResource(this.resources,R.mipmap.ic_launcher_rem_round)).setContentIntent(pendingIntent)


            }else{ builder = Notification.Builder(this).setContent(contentView).setSmallIcon(R.mipmap.ic_launcher_rem_round).setLargeIcon(BitmapFactory.decodeResource(this.resources,R.mipmap.ic_launcher_rem_round)).setContentIntent(pendingIntent)}

            notificationManager.notify(1234,builder.build()) }




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
            println("--------------------Tablet-------------")
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
        const val ID = "id"

        private lateinit var mTestcontext: Context

    }
}