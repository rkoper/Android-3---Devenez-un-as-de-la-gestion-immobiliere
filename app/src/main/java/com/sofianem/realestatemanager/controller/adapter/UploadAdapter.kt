package com.sofianem.realestatemanager.controller.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sofianem.realestatemanager.R.*
import com.sofianem.realestatemanager.utils.MyCommunicationForImage
import com.sofianem.realestatemanager.utils.Utils


class UploadAdapter(
    var mListPath: MutableList<String?>,
    var mListDescription: MutableList<String?>,
    var mListId: MutableList<Int?>,
    var mContext: Context
) : RecyclerView.Adapter<UploadAdapter.UploadViewHolder>() {
    private val mMyCommunicator = mContext as MyCommunicationForImage

    override fun getItemCount() = mListDescription.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(layout.list_item_upload, parent, false)
        return UploadViewHolder(v) }


    override fun onBindViewHolder(holder: UploadViewHolder, position: Int) {


        if (mListDescription[position].isNullOrBlank() && mListPath[position].isNullOrBlank()  ) {
            Toast.makeText(mContext, "no photo", Toast.LENGTH_SHORT).show() }

        else { holder.tvTxt.text = mListDescription[position];
            val p = Utils.rotateImage(mListPath[position])
            holder.tvPhoto.setImageBitmap(p) }

        holder.tvIcon.setOnClickListener {
                mMyCommunicator.deleteImage(mListId[position]!!.toInt(),
                mListDescription[position]!!.toString(),
                mListPath[position]!!.toString()) }


        holder.txtIcon.setOnClickListener {
            mMyCommunicator.uploadImage(mListId[position]!!.toInt()) } }



    class UploadViewHolder(v: View) : RecyclerView.ViewHolder(v) {
       var tvIcon: ImageButton = itemView.findViewById(id.delete_upload)
        var tvPhoto: ImageView = itemView.findViewById(id.activity_upload_item_imageview)
        var txtIcon: ImageButton = itemView.findViewById(id.edit_upload)
        var tvTxt: TextView = itemView.findViewById(id.upload_item_txt) }

}
