package com.epitech.mael.epicture

import android.app.PendingIntent.getActivity
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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

import retrofit2.Call
import retrofit2.Response
import com.epitech.mael.epicture.Imgur.*
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.content_main.*
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import java.io.ByteArrayOutputStream
import okhttp3.*


@Suppress("INACCESSIBLE_TYPE")
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            UploadImage()
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

        ApiHandler().getService(accessToken, null).getAvatar(username).enqueue(object : retrofit2.Callback<Avatar> {

            override fun onResponse(call: Call<Avatar>, response: Response<Avatar>) {
                val url = response.body()?.avatarUrl()
                Picasso.get().load(url).transform(CropCircleTransformation()).into(NavUsernameImage)
            }

            override fun onFailure(call: Call<Avatar>, t: Throwable) {
                println("Error fetch Avatar")
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
        when (item.itemId) {

            R.id.nav_my_pictures -> {
                DisplayUserImages()
            }
            R.id.nav_search -> {

            }
            R.id.nav_upload -> {
                UploadImage()
            }
            R.id.nav_favorites -> {

            }
            R.id.nav_logout -> {
                LogoutUser()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun DisplayUserImages() {
        val username = intent.getStringExtra("username")
        val accessToken = intent.getStringExtra("accessToken")

        ApiHandler().getService(accessToken, null).getUserImages(username).enqueue(object : retrofit2.Callback<ImageList> {
            override fun onResponse(call: Call<ImageList>, response: Response<ImageList>) {
                val payload = response.body()!!.data
                runOnUiThread {
                    recyclerView_main.adapter = UserImagesAdapter(payload, accessToken)
                }
            }

            override fun onFailure(call: Call<ImageList>, t: Throwable) {
                Log.i("nFailure:", "FAIL")
            }
        })
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 123 && resultCode == RESULT_OK && data != null) {
            val pickedImage = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, pickedImage)
            val encodedBitmap = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100)

            val accessToken = intent.getStringExtra("accessToken")
            val mediaType = MediaType.parse("text/plain")
            val body = RequestBody.create(mediaType, encodedBitmap)
            Toast.makeText(applicationContext,  "Uploading...",
                    Toast.LENGTH_LONG).show()
            ApiHandler().getService(accessToken, body).getUploadResponse(body).enqueue(object: retrofit2.Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

                }
            })
        }

    }

    private fun encodeToBase64(image: Bitmap, compressFormat: Bitmap.CompressFormat, quality: Int): String {
        val byteArrayOS = ByteArrayOutputStream()
        image.compress(compressFormat, quality, byteArrayOS)
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT)
    }

    private fun UploadImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 123)
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