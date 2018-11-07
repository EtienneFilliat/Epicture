package com.epitech.mael.epicture

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.CookieManager
import android.webkit.ValueCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.epitech.mael.epicture.Adapters.AlbumAdapter
import com.epitech.mael.epicture.Adapters.ImagesAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

import retrofit2.Call
import retrofit2.Response
import com.epitech.mael.epicture.Imgur.*
import kotlinx.android.synthetic.main.content_main.*

@Suppress("INACCESSIBLE_TYPE")
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        Log.e("TOKEN", intent.getStringExtra("accessToken"))

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        recyclerView_main.layoutManager = LinearLayoutManager(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        val username = intent.getStringExtra("username")
        val accessToken = intent.getStringExtra("accessToken")

        ApiHandler().getService(accessToken).getAvatar(username).enqueue(object : retrofit2.Callback<Avatar> {

            override fun onResponse(call: Call<Avatar>, response: Response<Avatar>) {
                val url = response.body()?.avatarUrl()
                Glide.with(this@MainActivity)
                        .load(url)
                        .apply(RequestOptions.circleCropTransform())
                        .into(NavUsernameImage)
            }

            override fun onFailure(call: Call<Avatar>, t: Throwable) {
                Log.e("onCreateOptionsMenu", "Unable to load user avatar")
            }
        })

        NavUsername.setText(username)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        val username = intent.getStringExtra("username")
        val accessToken = intent.getStringExtra("accessToken")

        when (item.itemId) {

            R.id.nav_my_pictures -> {
                DisplayUserImages()
            }
            R.id.nav_search -> {

            }
            R.id.nav_upload -> {

            }
            R.id.nav_favorites -> {
                DisplayFavoriteImages()
            }
            R.id.nav_logout -> {
                LogoutUser()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun DisplayFavoriteImages() {
        val username = intent.getStringExtra("username")
        val accessToken = intent.getStringExtra("accessToken")

        ApiHandler().getService(accessToken).getUserFavorites(username).enqueue(object : retrofit2.Callback<AlbumList> {
            override fun onResponse(call: Call<AlbumList>, response: Response<AlbumList>) {
                val payload = response.body()!!.data
                runOnUiThread {
                    recyclerView_main.adapter = AlbumAdapter(payload, accessToken, R.layout.image_item_row)
                }
            }

            override fun onFailure(call: Call<AlbumList>, t: Throwable) {
                Log.e("DisplayFavoriteImages:", "Couldn't display User Favorites")
            }
        })
    }

    private fun DisplayUserImages() {
        val username = intent.getStringExtra("username")
        val accessToken = intent.getStringExtra("accessToken")

        ApiHandler().getService(accessToken).getUserImages(username).enqueue(object : retrofit2.Callback<ImageList> {
            override fun onResponse(call: Call<ImageList>, response: Response<ImageList>) {
                val payload = response.body()!!.data
                runOnUiThread {
                    recyclerView_main.adapter = ImagesAdapter(payload, accessToken, R.layout.image_item_row)
                }
            }

            override fun onFailure(call: Call<ImageList>, t: Throwable) {
                Log.e("DisplayUserImages:", "Couldn't display UserImages")
            }
        })
    }

    private fun LogoutUser()
    {
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookies(ValueCallback<Boolean> { value ->
            Log.d("LOGOUT", "Cookie removed: " + value)
        })
        val newIntent = Intent(this, LoginScreen::class.java)
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(newIntent)
    }
}