package com.sofianem.realestatemanager.controller.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sofianem.realestatemanager.R
import kotlin.coroutines.coroutineContext

class PlacesListAdapter(
    private val mListName: ArrayList<String>,
    private val mListDistance: ArrayList<String>,
    val mType: String,
    val mContext: Context
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

        if (mType =="park" ) { goGreen(holder) }
        if (mType =="supermarket" ) { goRed(holder) }
        if (mType == "primary_school" ) { goYellow(holder) }
        if (mType == "pharmacy") { goBlue(holder) }


    }

    private fun goRed(holder: PlaceViewHolder) { holder.linearColor.setBackgroundColor(Color.RED) }
    private fun goGreen(holder: PlaceViewHolder) { holder.linearColor.setBackgroundColor(Color.GREEN) }
    private fun goBlue(holder: PlaceViewHolder){ holder.linearColor.setBackgroundColor(Color.BLUE) }
    private fun goYellow(holder: PlaceViewHolder) { holder.linearColor.setBackgroundColor(Color.YELLOW) }


    class PlaceViewHolder(placeItem: View) : RecyclerView.ViewHolder(placeItem) {
        var name: TextView;  var distance: TextView
        var linearColor : LinearLayout
        init { placeItem.isClickable = false
            name = placeItem.findViewById(com.sofianem.realestatemanager.R.id.nameof)
            distance = placeItem.findViewById(com.sofianem.realestatemanager.R.id.distance)
            linearColor = placeItem.findViewById(com.sofianem.realestatemanager.R.id.linearColor)
        }
    }
}
