package com.sofianem.realestatemanager.controller.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
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
    private var mContext: Context,
    private var  mTablet: Boolean?) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {


    private var l:ArrayList<String?> = arrayListOf()
    private var mSelectedPos = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        l.clear()
        return MainViewHolder(v) }

    override fun getItemCount(): Int { return mMyDataset?.size!! }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        println("      mTablet          $mTablet")
        if (mTablet!!) { if (mSelectedPos == position) {
            holder.tvColor.setBackgroundResource(R.color.colorD)
            holder.tvCity.setTextColor(ContextCompat.getColor(mContext, R.color.colorB))
            holder.tvPrice.setTextColor(ContextCompat.getColor(mContext, R.color.colorB))
            holder.tvType.setTextColor(ContextCompat.getColor(mContext, R.color.colorB))
        }

        else {
            holder.tvColor.setBackgroundResource(R.color.colorB)
            holder.tvCity.setTextColor(ContextCompat.getColor(mContext, R.color.colorD))
            holder.tvPrice.setTextColor(ContextCompat.getColor(mContext, R.color.colorD))
            holder.tvType.setTextColor(ContextCompat.getColor(mContext, R.color.colorD)) } }

        if (mMyDataset!![position].status == "sold")
        {holder.tvSold.isVisible = true}

        if (mMyDataset!![position].type == "Type") {holder.tvType.text = "-"}
        else{ holder.tvType.text = mMyDataset!![position].type}

        if (mMyDataset!![position].price == 0) {holder.tvPrice.text = "-"}
        else{ val mPrice = Utils.addWhiteSpace(mMyDataset!![position].price.toString())
            val mDisplayPrice = "$mPrice $"
            holder.tvPrice.text = mDisplayPrice
        }

        holder.tvCity.text = mMyDataset!![position].city

        mMyDatasetImage!!.forEach {
            if (mMyDataset!![position].id == it.masterId) { l.add(it.imageUri)
                if (l.size > 1)
                    return@forEach
                val p = Utils.rotateImage(it.imageUri)
                Glide.with(mContext).load(p).into(holder.tvPhoto) } }

        holder.itemView.setOnClickListener {
            val myCommunicator = mContext as MyCommunication
            myCommunicator.displayDetails(mMyDataset!![position].id)

            if (mTablet!!){
                if(mSelectedPos==position){ mSelectedPos=-1 }
                mSelectedPos = position
                notifyDataSetChanged()} }
    }


    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvColor :ConstraintLayout = itemView.findViewById(R.id.twoMain)
        var tvType: TextView = itemView.findViewById(R.id.activity_main_item_type)
        var tvCity: TextView = itemView.findViewById(R.id.activity_main_item_city)
        var tvPrice: TextView = itemView.findViewById(R.id.activity_main_item_price)
        var tvPhoto: ImageView = itemView.findViewById(R.id.activity_main_item_imageview)
        var tvSold: ConstraintLayout = itemView.findViewById(R.id.item_list_sold)}


}