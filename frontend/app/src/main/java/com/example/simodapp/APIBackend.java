package com.example.simodapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIBackend {

    private static final String URL = "http://10.0.2.2:8080/";

    //biblioteca para Android que serve como ponte entre o aplicativo e o backend
    private static Retrofit retrofit;
    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;

    }

}
