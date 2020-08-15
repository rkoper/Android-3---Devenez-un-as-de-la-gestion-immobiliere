package com.sofianem.realestatemanager.controller.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sofianem.realestatemanager.R
import com.sofianem.realestatemanager.controller.adapter.DetailAdapter.DetailViewHolder
import com.sofianem.realestatemanager.utils.Utils
import java.io.File


class DetailAdapter(
    private val mListImage: MutableList<String?>,
    private val mListImageDescription: MutableList<String?>,
    private val mContext: Context
) :
    RecyclerView.Adapter<DetailViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_detail, parent, false)
        return DetailViewHolder(v) }


    override fun getItemCount() = mListImage.size


    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {

        if (mListImage[position].isNullOrBlank() && mListImageDescription[position].isNullOrBlank()  ) {
            Toast.makeText(mContext, "no photo", Toast.LENGTH_SHORT).show() }
        else {
            val p = Utils.rotateImage(mListImage[position])
            Glide.with(mContext).load(p).override(200).into(holder.tvPhoto)
            holder.txtPhoto.text = mListImageDescription[position]}
        holder.setListeners(position, mContext, mListImage, mListImageDescription) }


    class DetailViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var txtPhoto: TextView = itemView.findViewById(R.id.activity_detail_item_text_description)
        var tvPhoto: ImageView = itemView.findViewById(R.id.activity_detail_item_imageview)
        @SuppressLint("ResourceAsColor")
        fun setListeners(position: Int, context: Context, mListImage: MutableList<String?>, listimageDescription: MutableList<String?>)
        { itemView.setOnClickListener {
            val mDialogView = LayoutInflater.from(it.context).inflate(R.layout.dialog_imageview_rvdetail, null)
            mDialogView.requestFocus()
            val builder = AlertDialog.Builder(it.context).setView(mDialogView)
            val o: ImageView = mDialogView.findViewById(R.id.dialog_imageview)
            val p = Utils.rotateImage(File(mListImage[position]).absolutePath)
            o.setImageBitmap(p)
            val q: TextView = mDialogView.findViewById(R.id.dialog_imageview_text)
            q.text = listimageDescription[position]

            builder.show()
        } } }
}
