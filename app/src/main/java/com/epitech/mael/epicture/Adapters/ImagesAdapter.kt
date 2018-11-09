package com.epitech.mael.epicture.Adapters

import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.epitech.mael.epicture.Imgur.ImageList.Image
import kotlinx.android.synthetic.main.image_item_row.view.*

class ImagesAdapter(private val data: List<Image>, private val token: String, private val layout: Int) : RecyclerView.Adapter<ImagesAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val row = layoutInflater.inflate(layout, parent, false)
        return CustomViewHolder(row)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val image = data[position]
        val thumbnailImageView = holder.view.iv_photo
        Glide.with(holder.view)
                .load(image.link)
                .into(thumbnailImageView)
        if (!image.title.isNullOrBlank())
            holder.view.iv_title.setText(image.title)
        if (!image.description.isNullOrBlank())
            holder.view.iv_description.setText(image.description)
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}
