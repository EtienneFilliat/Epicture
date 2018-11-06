package com.epitech.mael.epicture.Imgur;

import retrofit2.Call;
import retrofit2.http.*;

public interface ImgurApi {
    /*
    ** Imgur's api in retrofit format.
    */
    @GET("account/{user}/avatar")
    Call<Avatar> getAvatar(@Path("user") String user);
}
