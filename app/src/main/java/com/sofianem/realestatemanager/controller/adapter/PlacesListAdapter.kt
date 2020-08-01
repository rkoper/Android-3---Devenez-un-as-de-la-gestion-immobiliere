package com.sofianem.realestatemanager.controller.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlacesListAdapter(
    private val mListName: ArrayList<String>,
    private val mListDistance: ArrayList<String>,
    val  mType: String
) :
    RecyclerView.Adapter<PlacesListAdapter.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.sofianem.realestatemanager.R.layout.list_item_detail_map, parent, false)

        return PlaceViewHolder(view) }


    override fun getItemCount() = mListName.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.name.text = mListName[position]
        holder.distance.text = mListDistance[position] + " m"

        if (mType =="park" ) { holder.colorLeft.setBackgroundColor(Color.GREEN)}
        if (mType =="supermarket" ) {  holder.colorLeft.setBackgroundColor(Color.RED)}
        if (mType == "primary_school" ) {  holder.colorLeft.setBackgroundColor(Color.YELLOW)}
        if (mType == "pharmacy") { holder.colorLeft.setBackgroundColor(Color.BLUE)}
 }


    class PlaceViewHolder(placeItem: View) : RecyclerView.ViewHolder(placeItem) {
        var name: TextView;  var distance: TextView;  var colorLeft : LinearLayout
        init { placeItem.isClickable = false
            name = placeItem.findViewById(com.sofianem.realestatemanager.R.id.nameof)
            distance = placeItem.findViewById(com.sofianem.realestatemanager.R.id.distance)
            colorLeft = placeItem.findViewById(com.sofianem.realestatemanager.R.id.clForColor)
        }
    }
}