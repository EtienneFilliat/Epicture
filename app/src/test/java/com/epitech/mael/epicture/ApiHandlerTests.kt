package com.epitech.mael.epicture

import com.epitech.mael.epicture.Imgur.ApiHandler
import com.epitech.mael.epicture.Imgur.Avatar
import com.epitech.mael.epicture.Imgur.ImageList
import com.epitech.mael.epicture.Imgur.ImgurApi
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

private const val ACCESSTOKEN = "ff5d80c2f00c16a9c5e7d935fb7c7e9df3fc3d4c"
private const val USER = "SpikiWax"

class ApiHandlerTest {

    @Test
    fun ApiHandler_Test_getAvatar() {
        ApiHandler().getService(ACCESSTOKEN, null).getAvatar(USER).enqueue(object : retrofit2.Callback<Avatar> {
            override fun onResponse(call: Call<Avatar>, response: Response<Avatar>) {
                val imageUrl = response.body()?.avatarUrl()
                assertThat(imageUrl == "https://imgur.com/user/spikiwax/avatar").isTrue()
                assertThat(response.isSuccessful).isTrue()
            }

            override fun onFailure(call: Call<Avatar>, t: Throwable) {
                assertThat(true).isFalse()
            }
        })
    }

    @Test
    fun ApiHandler_Test_toogleLike_up() {
        ApiHandler().getService(ACCESSTOKEN, null).toogleLike("NfYeZ6t", ImgurApi.Vote.up)
                .enqueue(object : retrofit2.Callback<com.epitech.mael.epicture.Imgur.Response> {
                    override fun onResponse(call: Call<com.epitech.mael.epicture.Imgur.Response>, response: Response<com.epitech.mael.epicture.Imgur.Response>) {
                        assertThat(response.isSuccessful).isTrue()
                    }

                    override fun onFailure(call: Call<com.epitech.mael.epicture.Imgur.Response>, t: Throwable) {
                        assertThat(true).isFalse()
                    }
                })
    }

    @Test
    fun ApiHandler_Test_toogleLike_down() {
        ApiHandler().getService(ACCESSTOKEN, null).toogleLike("NfYeZ6t", ImgurApi.Vote.down)
                .enqueue(object : retrofit2.Callback<com.epitech.mael.epicture.Imgur.Response> {
                    override fun onResponse(call: Call<com.epitech.mael.epicture.Imgur.Response>, response: Response<com.epitech.mael.epicture.Imgur.Response>) {
                        assertThat(response.isSuccessful).isTrue()
                    }

                    override fun onFailure(call: Call<com.epitech.mael.epicture.Imgur.Response>, t: Throwable) {
                        assertThat(true).isFalse()
                    }
                })
    }

    @Test
    fun ApiHandler_Test_toogleLike_veto() {
        ApiHandler().getService(ACCESSTOKEN, null).toogleLike("NfYeZ6t", ImgurApi.Vote.veto)
                .enqueue(object : retrofit2.Callback<com.epitech.mael.epicture.Imgur.Response> {
                    override fun onResponse(call: Call<com.epitech.mael.epicture.Imgur.Response>, response: Response<com.epitech.mael.epicture.Imgur.Response>) {
                        assertThat(response.isSuccessful).isTrue()
                    }

                    override fun onFailure(call: Call<com.epitech.mael.epicture.Imgur.Response>, t: Throwable) {
                        assertThat(true).isFalse()
                    }
                })
    }
}
