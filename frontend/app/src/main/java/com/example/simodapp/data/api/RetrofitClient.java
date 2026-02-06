package com.example.simodapp.data.api;

import android.util.Log;

import com.example.simodapp.util.SessionManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL_BACKEND = "http://10.0.2.2:8080/";   // seu backend local (emulador)
    private static final String BASE_URL_VIACEP   = "https://viacep.com.br/ws/";

    private static Retrofit retrofitBackend;
    private static Retrofit retrofitViaCep;

    // Cliente para o backend (com autenticação)
    public static Retrofit getClient(SessionManager sessionManager) {
        Log.d("RetrofitClient", "getClient chamado");

        if (retrofitBackend == null) {
            String token = sessionManager != null ? sessionManager.getToken() : "null";
            Log.d("RetrofitClient", "Token enviado: " + (token != null ? token.substring(0, Math.min(20, token.length())) + "..." : "null"));

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);  // Mostra headers, body, status

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(sessionManager))
                    .addInterceptor(logging)  // ← Adicione isso
                    .build();

            retrofitBackend = new Retrofit.Builder()
                    .baseUrl(BASE_URL_BACKEND)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            Log.d("RetrofitClient", "Cliente criado com logging BODY");
        }
        return retrofitBackend;
    }

    // Cliente para APIs públicas (sem autenticação) → ideal para ViaCEP
    public static Retrofit getPublicClient() {
        if (retrofitViaCep == null) {
            OkHttpClient client = new OkHttpClient.Builder().build(); // sem interceptor

            retrofitViaCep = new Retrofit.Builder()
                    .baseUrl(BASE_URL_VIACEP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofitViaCep;
    }
}