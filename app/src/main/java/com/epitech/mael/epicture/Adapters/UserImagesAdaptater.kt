package com.epitech.mael.epicture

import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.bumptech.glide.Glide
import com.epitech.mael.epicture.Imgur.ApiHandler
import com.epitech.mael.epicture.Imgur.ImageList
import com.epitech.mael.epicture.Imgur.Response
import android.support.v7.app.AlertDialog
import android.widget.Adapter
import android.widget.Toast
import kotlinx.android.synthetic.main.user_images_manager.view.*
import retrofit2.Call



class UserImagesAdaptater(private val data:  MutableList<ImageList.Image>, private val token: String) : RecyclerView.Adapter<UserImagesAdaptater.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserImagesAdaptater.CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val row = layoutInflater.inflate(R.layout.user_images_manager, parent, false)
        return CustomViewHolder(row)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        /*
        *   Setup useful variables
         */
        val image = data[position]
        val thumbnailImageView = holder.view.iv_photo
        val title = holder.view.iv_title
        val description = holder.view.iv_description
        val delButton = holder.view.iv_delete_button


        title.setText(image.title)
        description.setText(image.description)
        Glide.with(holder.view).load(image.link).into(thumbnailImageView)

        title.setOnEditorActionListener { view, action, _ ->
            if (action == EditorInfo.IME_ACTION_DONE) {
                ApiHandler()
                        .getService(token, null)
                        .updateImageInfos(image.deletehash, view.text.toString(), image.description)
                        .enqueue(object : retrofit2.Callback<Response> {
                            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {}
                            override fun onFailure(call: Call<Response>, t: Throwable) { Log.w("TitleChanger", "Failed to change Title") }
                        })
                true
            } else {
                false
            }
        }

        description.setOnEditorActionListener { view, action, event ->
            if (action == EditorInfo.IME_ACTION_DONE) {
                ApiHandler()
                        .getService(token, null)
                        .updateImageInfos(image.deletehash, image.title, view.text.toString())
                        .enqueue(object : retrofit2.Callback<Response> {
                            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {}
                            override fun onFailure(call: Call<Response>, t: Throwable) { Log.w("TitleChanger", "Failed to change Title") }
                        })
                true
            } else {
                false
            }
        }
        delButton.setOnClickListener { _ ->
            val pictureDialog = AlertDialog.Builder(holder.view.context)
            pictureDialog.setTitle("Delete this image ?")
            val pictureDialogItems = arrayOf<String>("Yes", "No")
            pictureDialog.setItems(pictureDialogItems, DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    0 -> {
                        ApiHandler().getService(token, null).deleteImage(image.id).enqueue(object : retrofit2.Callback<Response> {
                            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {}
                            override fun onFailure(call: Call<Response>, t: Throwable) { Log.w("DeleteImage", "Failed to delete an image") }

                        })
                        Toast.makeText(holder.view.context,  "Image deleted",
                                Toast.LENGTH_LONG).show()
                        data.removeAt(position)
                        notifyDataSetChanged()
                    }
                    1 -> {
                    }
                }
            })
            pictureDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}