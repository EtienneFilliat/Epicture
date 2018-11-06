package com.epitech.mael.epicture

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.epitech.mael.epicture.Imgur.ImageList
import kotlinx.android.synthetic.main.image_item_row.view.*

class UserImagesAdapter(private val data: List<ImageList.Image>, private val token: String) : RecyclerView.Adapter<UserImagesAdapter.CustomViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val row = layoutInflater.inflate(R.layout.image_item_row, parent, false)
        return CustomViewHolder(row)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val image = data[position]
        val thumbnailImageView = holder.view.iv_photo
        Glide.with(holder.view).load(image.link).into(thumbnailImageView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}
