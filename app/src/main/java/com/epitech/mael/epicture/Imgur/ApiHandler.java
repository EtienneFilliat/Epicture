package com.epitech.mael.epicture.Imgur;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import android.support.annotation.NonNull;

public final class ApiHandler {

    public ImgurApi getService(final String accessToken, final RequestBody body) {
        return retrofit(accessToken, body).create(ImgurApi.class);
    }

    private Retrofit retrofit(final String accessToken, final RequestBody body) {
        if (body != null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(@NonNull Chain chain) throws IOException {
                            return chain.proceed(
                                    chain.request()
                                            .newBuilder()
                                            .post(body)
                                            .addHeader("Authorization", "Bearer " + accessToken)
                                            .build()
                            );
                        }
                    }).build();
            return new Retrofit.Builder()
                    .baseUrl("https://api.imgur.com/3/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        else {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(@NonNull Chain chain) throws IOException {
                            return chain.proceed(
                                    chain.request()
                                            .newBuilder()
                                            .addHeader("Authorization", "Bearer " + accessToken)
                                            .build()
                            );
                        }
                    }).build();
            return new Retrofit.Builder()
                    .baseUrl("https://api.imgur.com/3/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }

    }
}

