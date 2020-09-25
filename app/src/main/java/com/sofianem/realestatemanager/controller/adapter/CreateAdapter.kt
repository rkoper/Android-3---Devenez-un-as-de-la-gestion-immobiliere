package com.sofianem.realestatemanager.controller.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sofianem.realestatemanager.utils.Utils


class CreateAdapter(
    private var mListOfNewImage: MutableList<String?>,
    private var mListimageDescription: MutableList<String?>,
    var mContext: Context
) : RecyclerView.Adapter<CreateAdapter.CreateViewHolder>() {

    override fun getItemCount() = mListimageDescription.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(com.sofianem.realestatemanager.R.layout.list_item_create, parent, false)
        return CreateViewHolder(v) }

    override fun onBindViewHolder(holder: CreateViewHolder, position: Int) {
        val p = Utils.rotateImage(mListOfNewImage[position])

        holder.tvPhoto.setImageBitmap(p)
        if (mListimageDescription[position].isNullOrBlank()) {
            Toast.makeText(mContext, "no photo", Toast.LENGTH_SHORT).show() }
        else { holder.tvTxt.text = mListimageDescription[position] } }

    class CreateViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvPhoto: ImageView = itemView.findViewById(com.sofianem.realestatemanager.R.id.activity_create_item_imageview)
        var tvTxt: TextView = itemView.findViewById(com.sofianem.realestatemanager.R.id.txt_photo) }
}
