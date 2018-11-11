package com.epitech.mael.epicture

import android.content.Context
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.epitech.mael.epicture.Adapters.AlbumAdapter
import com.epitech.mael.epicture.Imgur.AlbumList
import com.epitech.mael.epicture.Imgur.ApiHandler
import com.epitech.mael.epicture.Imgur.ImgurApi
import com.epitech.mael.epicture.R.layout.activity_search
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar((findViewById(R.id.search_bar)))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        search_list.layoutManager = LinearLayoutManager(this)
        search_list.hasFixedSize()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val accessToken = intent.getStringExtra("accessToken")
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        val searchAction = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean { return false }
            override fun onQueryTextSubmit(query: String?): Boolean {
                ApiHandler()
                        .getService(
                                accessToken,
                                null
                        ).getSearchedImages(
                                ImgurApi.Sort.time,
                                ImgurApi.Window.day,
                                0,
                                query
                        ).enqueue(object : retrofit2.Callback<AlbumList> {
                            override fun onResponse(call: Call<AlbumList>, response: Response<AlbumList>) {
                                var payload = response.body()!!.data
                                runOnUiThread { search_list.adapter = AlbumAdapter(payload, accessToken, R.layout.image_item_row) }
                            }

                            override fun onFailure(call: Call<AlbumList>, t: Throwable) { Log.e("SearchImages", "Failed to fetch Requested images") }
                        })
                return true
            }
        }
        searchView.setOnQueryTextListener(searchAction)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            // User chose the "Settings" item, show the app settings UI...
            true
        }

        R.id.action_favorite -> {
            // User chose the "Favorite" action, mark the current item
            // as a favorite...
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}