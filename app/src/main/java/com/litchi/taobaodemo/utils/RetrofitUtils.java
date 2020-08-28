package com.litchi.taobaodemo.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private RetrofitUtils() {
    }

    public static Retrofit getRetrofit(){
        return retrofit;
    }
}
