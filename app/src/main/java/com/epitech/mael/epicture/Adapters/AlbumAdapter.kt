package com.epitech.mael.epicture.Adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.epitech.mael.epicture.Imgur.AlbumList.Album
import com.epitech.mael.epicture.Imgur.ApiHandler
import com.epitech.mael.epicture.Imgur.ImgurApi
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
        val viewCounter = holder.view.iv_views
        val upvoteButton = holder.view.iv_upvote
        val downvoteButton = holder.view.iv_downvote

        viewCounter.text = album.views.toString()
        title.text = album.title
        if (album.is_album) {
            description.text = album.images[0].description
            Glide.with(holder.view).load(album.images[0].link).into(thumbnailImageView)
        } else {
            description.text = album.description
            Glide.with(holder.view).load(album.link).into(thumbnailImageView)
        }

        if (album.vote == "up")
            upvoteButton.setImageResource(R.drawable.ic_up_arrow_toogle)
        if (album.vote == "down")
            upvoteButton.setImageResource(R.drawable.ic_down_arrow_toogle)

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
                override fun onFailure(call: Call<Response>, t: Throwable) {
                    Log.w("FavoritesSwitcher", "Failed to favorite or un-favorite an image")
                }
            })
        }

        upvoteButton.setOnClickListener { _ ->
            if (album.vote == "veto" || album.vote == "down") {
                ApiHandler().getService(token, null).toogleLike(album.id, ImgurApi.Vote.up).enqueue(object : retrofit2.Callback<Response> {
                    override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                        upvoteButton.setImageResource(R.drawable.ic_up_arrow_toogle)
                        downvoteButton.setImageResource(R.drawable.ic_down_arrow_neutral)
                        album.vote = "up"
                    }

                    override fun onFailure(call: Call<Response>, t: Throwable) {
                        Log.w("VoteSwitcher", "Failed to switch to up")
                    }
                })
            } else {
                ApiHandler().getService(token, null).toogleLike(album.id, ImgurApi.Vote.veto).enqueue(object : retrofit2.Callback<Response> {
                    override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                        upvoteButton.setImageResource(R.drawable.ic_up_arrow_neutral)
                        downvoteButton.setImageResource(R.drawable.ic_down_arrow_neutral)
                        album.vote = "veto"
                    }

                    override fun onFailure(call: Call<Response>, t: Throwable) {
                        Log.w("VoteSwitcher", "Failed to switch from up to veto")
                    }
                })
            }
        }

        downvoteButton.setOnClickListener { _ ->
            if (album.vote == "veto" || album.vote == "up") {
                ApiHandler().getService(token, null).toogleLike(album.id, ImgurApi.Vote.down).enqueue(object : retrofit2.Callback<Response> {
                    override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                        upvoteButton.setImageResource(R.drawable.ic_up_arrow_neutral)
                        downvoteButton.setImageResource(R.drawable.ic_down_arrow_toogle)
                        album.vote = "down"
                    }

                    override fun onFailure(call: Call<Response>, t: Throwable) {
                        Log.w("VoteSwitcher", "Failed to switch to down")
                    }
                })
            } else {
                ApiHandler().getService(token, null).toogleLike(album.id, ImgurApi.Vote.veto).enqueue(object : retrofit2.Callback<Response> {
                    override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                        upvoteButton.setImageResource(R.drawable.ic_up_arrow_neutral)
                        downvoteButton.setImageResource(R.drawable.ic_down_arrow_neutral)
                        album.vote = "veto"
                    }

                    override fun onFailure(call: Call<Response>, t: Throwable) {
                        Log.w("VoteSwitcher", "Failed to switch from down to veto")
                    }
                })
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}