package com.example.simodapp.data.api;

import com.example.simodapp.data.dto.AddressRequest;
import com.example.simodapp.data.dto.FamilyRequest;
import com.example.simodapp.data.dto.LoginRequest;
import com.example.simodapp.data.dto.LoginResponse;
import com.example.simodapp.data.dto.RegisterRequest;
import com.example.simodapp.data.dto.RegisterResponse;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AuthApi {

    // ================= AUTH =================
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    // ================= ADDRESS =================
    @POST("auth/{patientId}/address")
    Call<Void> createAddress(
            @Path("patientId") UUID patientId,
            @Body AddressRequest request
    );

    // ================= FAMILY =================
    @POST("auth/{patientId}/family")
    Call<Void> createFamily(
            @Path("patientId") UUID patientId,
            @Body List<FamilyRequest> request
    );
}