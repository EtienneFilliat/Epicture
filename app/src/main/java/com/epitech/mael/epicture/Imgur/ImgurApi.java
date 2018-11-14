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

    /*
     *   Useful Enums
     */

    enum Sort {
        time,
        viral,
        top
    }

    enum Window {
        day,
        week,
        month,
        year,
        all
    }

    /*
     *   ==========================================
     *   =             GET CALLS                  =
     *   ==========================================
     */

    @GET("account/{user}/avatar")
    Call<Avatar> getAvatar(@Path("user") String user);

    @GET("account/{user}/images")
    Call<ImageList> getUserImages(@Path("user") String user);

    @GET("account/{user}/favorites")
    Call<AlbumList> getUserFavorites(@Path("user") String user);

    @GET("gallery/search/{sort}/{window}/{page}")
    Call<AlbumList> getSearchedImages(@Path("sort") Sort sort, @Path("window") Window window, @Path("page") int page, @Query("q") String query);

    /*
     *   ==========================================
     *   =             POST CALLS                 =
     *   ==========================================
     */

    @POST("{type}/{imageHash}/favorite")
    Call<Response> switchFavorites(@Path("type") String type, @Path("imageHash") String hash);

    @FormUrlEncoded
    @POST("image/{imageHash}")
    Call<Response> updateImageInfos(@Path("imageHash") String hash, @Field("title") String title, @Field("description") String desc);

    @POST("image")
    Call<ResponseBody> getUploadResponse(@Body RequestBody body);


    /*
     *   ==========================================
     *   =             DELETE CALLS                 =
     *   ==========================================
     */

    @DELETE("image/{imageHash}")
    Call<Response> deleteImage(@Path(  "imageHash") String hash);
}