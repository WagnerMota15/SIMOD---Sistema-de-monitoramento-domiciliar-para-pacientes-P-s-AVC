package com.example.simodapp;

<<<<<<< HEAD
import com.example.simodapp.login.LoginRequest;
import com.example.simodapp.login.LoginResponse;

=======
>>>>>>> b41d5a546f4588b9e6935e6343a4affe68e7555c
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIService {
<<<<<<< HEAD
    @POST("usuario")
    Call<User> criarUsuario(@Body UserRequest userRequest);
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);
=======

    @POST("usuario")
    Call<User> criarUsuario(@Body UserRequest userRequest);
>>>>>>> b41d5a546f4588b9e6935e6343a4affe68e7555c
}
