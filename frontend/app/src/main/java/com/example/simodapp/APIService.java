package com.example.simodapp;

import com.example.simodapp.login.LoginRequest;
import com.example.simodapp.login.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIService {
    @POST("usuario")
    Call<User> criarUsuario(@Body UserRequest userRequest);
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);
}
