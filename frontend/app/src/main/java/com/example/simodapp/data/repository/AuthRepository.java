package com.example.simodapp.data.repository;

import com.example.simodapp.data.api.AuthApi;
import com.example.simodapp.data.api.RetrofitClient;
import com.example.simodapp.data.dto.LoginRequest;
import com.example.simodapp.data.dto.LoginResponse;
import com.example.simodapp.data.dto.RegisterRequest;
import com.example.simodapp.data.dto.RegisterResponse;
import com.example.simodapp.domain.enums.Role;
import com.example.simodapp.domain.enums.StrokeTypes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private final AuthApi authApi;

    public AuthRepository(){
        authApi = RetrofitClient.getClient().create(AuthApi.class);
    }

    public void login(String login, String password, LoginCallback callback){
        LoginRequest loginRequest = new LoginRequest(login,password);

        authApi.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
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


    public void register(String nameComplete, String cpf, String email, String password, String telephone, Role role, StrokeTypes strokeTypes, String numCouncil, RegisterCallback callback){
        RegisterRequest request = new RegisterRequest(nameComplete, cpf, email, password, telephone, role, strokeTypes, numCouncil);
        authApi.register(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if(response.isSuccessful()&&response.body()!=null){
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
