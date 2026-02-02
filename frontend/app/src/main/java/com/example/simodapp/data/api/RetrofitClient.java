package com.example.simodapp.data.api;

import com.example.simodapp.util.SessionManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String URL = "http://10.0.2.2:8080/";

    //biblioteca para Android que serve como ponte entre o aplicativo e o backend
    private static Retrofit retrofit;
    public static Retrofit getClient(SessionManager sessionManager){
        if(retrofit == null){

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(sessionManager))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;

    }

}
