package com.example.simodapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIService {

    @POST("usuario")
    Call<User> criarUsuario(@Body UserRequest userRequest);
}
