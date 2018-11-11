package com.epitech.mael.epicture.Adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.epitech.mael.epicture.Imgur.AlbumList.Album
import com.epitech.mael.epicture.Imgur.ApiHandler
import com.epitech.mael.epicture.Imgur.Response
import com.epitech.mael.epicture.R
import kotlinx.android.synthetic.main.image_item_row.view.*
import retrofit2.Call

class AlbumAdapter(private val data: List<Album>, private val token: String, private val layout: Int) : RecyclerView.Adapter<AlbumAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumAdapter.CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val row = layoutInflater.inflate(layout, parent, false)
        return AlbumAdapter.CustomViewHolder(row)
    }

    override fun onBindViewHolder(holder: AlbumAdapter.CustomViewHolder, position: Int) {
        /*
        *   Setup useful variables
         */
        val album = data[position]
        val type = if (album.is_album) "album" else "image"
        val thumbnailImageView = holder.view.iv_photo
        val title = holder.view.iv_title
        val description = holder.view.iv_description
        val favButton = holder.view.iv_favorite_button

        title.text = album.title
        if (album.is_album) {
            description.text = album.images[0].description
            Glide.with(holder.view).load(album.images[0].link).into(thumbnailImageView)
        } else {
            description.text = album.description
            Glide.with(holder.view).load(album.link).into(thumbnailImageView)
        }

        if (album.favorite)
            favButton.setImageResource(R.drawable.ic_fav)
        else
            favButton.setImageResource(R.drawable.ic_no_fav)

        favButton.setOnClickListener { _ ->
            if (album.favorite) {
                album.favorite = false
                favButton.setImageResource(R.drawable.ic_no_fav)
            } else {
                album.favorite = true
                favButton.setImageResource(R.drawable.ic_fav)
            }

            ApiHandler().getService(token, null).switchFavorites(type, album.id).enqueue(object : retrofit2.Callback<Response> {
                override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {}
                override fun onFailure(call: Call<Response>, t: Throwable) { Log.w("FavoritesSwitcher", "Failed to favorite or un-favorite an image") }
            })
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}
