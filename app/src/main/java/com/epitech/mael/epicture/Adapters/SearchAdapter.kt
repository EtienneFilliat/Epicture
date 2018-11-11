package com.epitech.mael.epicture.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.epitech.mael.epicture.Imgur.AlbumList.Album
import com.epitech.mael.epicture.R

class SearchAdapter(private val data: List<Album>, private val token: String, private val layout: Int) : RecyclerView.Adapter<SearchAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val row = layoutInflater.inflate(R.layout.search_view, parent, false)
        return SearchAdapter.CustomViewHolder(row)
    }

    override fun onBindViewHolder(p0: CustomViewHolder, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}