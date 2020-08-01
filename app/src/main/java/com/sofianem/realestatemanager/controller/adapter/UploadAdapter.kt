package com.sofianem.realestatemanager.controller.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.sofianem.realestatemanager.R.*
import com.sofianem.realestatemanager.utils.MyCommunicationV2
import com.sofianem.realestatemanager.utils.Utils


class UploadAdapter(var mListPath: MutableList<String?>, var mListDescription: MutableList<String?>, var mContext: Context
) : RecyclerView.Adapter<UploadAdapter.UploadViewHolder>() {
    private val mMyCommunicator = mContext as MyCommunicationV2

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
            mMyCommunicator.displayup(mListPath[position].toString())
            mListPath.removeAt(position) ;   mListDescription.removeAt(position)
            notifyDataSetChanged() }


        holder.txtIcon.setOnClickListener {
            mMyCommunicator.displayupv2(mListPath[position])
            notifyDataSetChanged() } }


    class UploadViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvIcon: ImageView = itemView.findViewById(id.delete_upload)
        var tvPhoto: ImageView = itemView.findViewById(id.activity_upload_item_imageview)
        var txtIcon: ImageView = itemView.findViewById(id.edit_upload)
        var tvTxt: TextView = itemView.findViewById(id.upload_item_txt) }

}
