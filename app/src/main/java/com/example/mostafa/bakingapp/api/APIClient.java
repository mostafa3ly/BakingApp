package com.example.mostafa.bakingapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mosta on 10/3/2018.
 */

public class APIClient {

    private static Retrofit retrofit = null;
    private static final String BASE_URL =  "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    private APIClient(){

    }
    public static Retrofit getClient()
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
