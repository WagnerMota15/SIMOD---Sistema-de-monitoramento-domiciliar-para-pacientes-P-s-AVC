package com.example.simodapp.data.api;

import com.example.simodapp.data.dto.LoginRequest;
import com.example.simodapp.data.dto.LoginResponse;
import com.example.simodapp.data.dto.RegisterRequest;
import com.example.simodapp.data.dto.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);


    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

}
