package com.epitech.mael.epicture.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.epitech.mael.epicture.Imgur.AlbumList.Album
import com.epitech.mael.epicture.R
import kotlinx.android.synthetic.main.image_item_row.view.*

class AlbumAdapter(private val data: List<Album>, private val token: String, private val layout: Int) : RecyclerView.Adapter<AlbumAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val row = layoutInflater.inflate(layout, parent, false)
        return CustomViewHolder(row)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val image = data[position]
        val thumbnailImageView = holder.view.iv_photo
        if (image.is_album)
            Glide.with(holder.view).load(image.images[0].link).into(thumbnailImageView)
        else
            Glide.with(holder.view).load(image.link).into(thumbnailImageView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}
