package com.example.simodapp.data.api;

import com.example.simodapp.data.dto.AddressRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AddressApi {

    @POST("/address")
    Call<Void> saveAddress(@Body AddressRequest request);
}
