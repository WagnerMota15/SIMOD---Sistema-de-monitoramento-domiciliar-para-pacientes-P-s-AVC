package com.example.simodapp.data.repository;

import com.example.simodapp.data.api.AuthApi;
import com.example.simodapp.data.api.RetrofitClient;
import com.example.simodapp.data.dto.LoginRequest;
import com.example.simodapp.data.dto.LoginResponse;
import com.example.simodapp.data.dto.RegisterRequest;
import com.example.simodapp.data.dto.RegisterResponse;
import com.example.simodapp.domain.enums.Role;
import com.example.simodapp.domain.enums.StrokeTypes;
import com.example.simodapp.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private final AuthApi authApi;
    private final SessionManager sessionManager;

    public AuthRepository(AuthApi authApi,SessionManager sessionManager){

        this.authApi = authApi;
        this.sessionManager = sessionManager;
    }

    public void login(LoginRequest loginRequest, LoginCallback callback){

        authApi.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful() && response.body()!= null){
                    sessionManager.saveToken(response.body().getToken());
                    callback.onSucess(response.body());
                } else {
                    callback.onError("Credenciais inválidas");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onError("ERRO DE CONEXÃO");
            }
        });

    }


    public void register(RegisterRequest registerRequest, RegisterCallback callback){

        authApi.register(registerRequest).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if(response.isSuccessful()&&response.body()!=null){

                    if(response.body().getToken()!=null){
                        sessionManager.saveToken(response.body().getToken());
                    }

                    callback.registerSucess(response.body());
                } else {
                    callback.registerError("Erro ao cadastrar usuário");
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                callback.registerError("ERRO DE CONEXÃO");
            }
        });

    }



}
