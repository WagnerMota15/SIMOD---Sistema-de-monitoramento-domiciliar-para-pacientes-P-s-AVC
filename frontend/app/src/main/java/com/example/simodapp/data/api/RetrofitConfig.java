package com.example.simodapp.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {

    private static final Retrofit retrofit = new Retrofit.Builder().baseUrl("https://viacep.com.br/").addConverterFactory(GsonConverterFactory.create()).build();

    public static CepService getCepService(){
        return retrofit.create(CepService.class);
    }

}
