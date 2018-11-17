package com.epitech.mael.epicture

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AlertDialog
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

    var _sort : ImgurApi.Sort = ImgurApi.Sort.time;
    var _window : ImgurApi.Window = ImgurApi.Window.day;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar((findViewById(R.id.search_bar)))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

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
                                _sort,
                                _window,
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
            val sortByDialog = AlertDialog.Builder(this)
            sortByDialog.setTitle("Sort by")
            val sortByDialogItems = arrayOf<String>("Time", "Viral", "Top")
            sortByDialog.setItems(sortByDialogItems, DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    0 -> {
                        _sort = ImgurApi.Sort.time
                    }
                    1 -> {
                        _sort = ImgurApi.Sort.viral
                    }
                    2 -> {
                        _sort = ImgurApi.Sort.top
                    }
                }
            })
            sortByDialog.show()
            var timeFrameDialog = AlertDialog.Builder(this)
            timeFrameDialog.setTitle("Select time frame")
            val timeFrameDialogItems = arrayOf<String>("Day", "Week", "Month", "Year", "All")
            timeFrameDialog.setItems(timeFrameDialogItems, DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    0 -> {
                        _window = ImgurApi.Window.day
                    }
                    1 -> {
                        _window = ImgurApi.Window.week
                    }
                    2 -> {
                        _window = ImgurApi.Window.month
                    }
                    3 -> {
                        _window = ImgurApi.Window.year
                    }
                    4 -> {
                        _window = ImgurApi.Window.all
                    }
                }
            })
            timeFrameDialog.show()
            true
        }

        R.id.action_favorite -> {
            // User chose the "Favorite" action, mark the current item
            // as a favorite...
            true
        }
        android.R.id.home -> {
            this.finish()
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}