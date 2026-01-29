package com.example.simodapp.data.api;

import com.example.simodapp.data.dto.CepResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CepService {

    @GET("ws/{cep}/json")
    Call<CepResponse> searchCep(@Path("cep") String cep);
}
