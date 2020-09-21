package com.sofianem.realestatemanager.controller.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sofianem.realestatemanager.R
import com.sofianem.realestatemanager.data.model.EstateR
import com.sofianem.realestatemanager.data.model.ImageV
import com.sofianem.realestatemanager.utils.MyCommunication
import com.sofianem.realestatemanager.utils.Utils


class MainAdapter(
    private var mMyDataset: List<EstateR>?,
    private var mMyDatasetImage: List<ImageV>?,
    private var mContext: Context) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {


    var l:ArrayList<String?> = arrayListOf()
    var mSelected_pos = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        println("--------------------Tablet-------3-------")
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        l.clear()
        return MainViewHolder(v) }

    override fun getItemCount(): Int { return mMyDataset?.size!! }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        if (mSelected_pos == position) { holder.tvTest.visibility = View.VISIBLE }
        else { holder.tvTest.visibility = View.INVISIBLE }



        if (mMyDataset!![position].status == "sold")
        {holder.tvSold.isVisible = true}

        if (mMyDataset!![position].type == "Type") {holder.tvType.text = "-"}
        else{ holder.tvType.text = mMyDataset!![position].type}

        if (mMyDataset!![position].price.equals( 0) ) {holder.tvPrice.text = "-"}
        else{ val mPrice = Utils.addWhiteSpace(mMyDataset!![position].price.toString())
            holder.tvPrice.text = mPrice + " $" }

        holder.tvCity.text = mMyDataset!![position].city

        mMyDatasetImage!!.forEach {
            if (mMyDataset!![position].id == it.masterId) { l.add(it.imageUri)
                if (l.size > 1)
                return@forEach
                val p = Utils.rotateImage(it.imageUri)
                Glide.with(mContext).load(p).into(holder.tvPhoto) } }

        holder.itemView.setOnClickListener {
            if(mSelected_pos==position){ mSelected_pos=-1 ; notifyDataSetChanged() }
            mSelected_pos = position;
            notifyDataSetChanged();

            val myCommunicator = mContext as MyCommunication
            mSelected_pos = position
            myCommunicator.displayDetails(mMyDataset!![position].id) }

            l.clear() }


    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTest: LinearLayout = itemView.findViewById(R.id.testll)
        var tvType: TextView = itemView.findViewById(R.id.activity_main_item_type)
        var tvCity: TextView = itemView.findViewById(R.id.activity_main_item_city)
        var tvPrice: TextView = itemView.findViewById(R.id.activity_main_item_price)
        var tvPhoto: ImageView = itemView.findViewById(R.id.activity_main_item_imageview)
        var tvSold = itemView.findViewById<ConstraintLayout>(R.id.item_list_sold)}

}
