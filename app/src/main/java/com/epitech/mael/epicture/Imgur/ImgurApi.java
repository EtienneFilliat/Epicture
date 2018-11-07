package com.epitech.mael.epicture.Imgur;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ImgurApi {
    /*
    ** Imgur's api in retrofit format.
    */
    @GET("account/{user}/avatar")
    Call<Avatar> getAvatar(@Path("user") String user);

    @GET("account/{user}/images")
    Call<ImageList> getUserImages(@Path("user") String user);

    @POST("image")
    Call<ResponseBody> getUploadResponse(@Body RequestBody body);
}
