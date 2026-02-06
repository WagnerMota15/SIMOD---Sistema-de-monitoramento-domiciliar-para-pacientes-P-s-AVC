package com.example.simodapp.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.simodapp.data.api.AuthApi;
import com.example.simodapp.data.dto.AddressRequest;
import com.example.simodapp.data.dto.FamilyRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressFamilyRepository {

    private static final String TAG = "AddressFamilyRepo";

    private final AuthApi api;

    public AddressFamilyRepository(AuthApi api) {
        this.api = api;
        Log.d(TAG, "AddressFamilyRepository criado com AuthApi");
    }

    public void sendAddressAndFamily(
            UUID patientId,
            AddressRequest address,
            List<FamilyRequest> familyList,
            MutableLiveData<Boolean> success,
            MutableLiveData<String> error
    ) {
        Log.d(TAG, "sendAddressAndFamily iniciado - patientId: " + patientId);

        if (patientId == null) {
            Log.e(TAG, "patientId NULL - abortando");
            error.postValue("ID do paciente inválido");
            return;
        }

        // 1. Salva o ENDEREÇO
        Log.d(TAG, "Enviando endereço...");
        api.createAddress(patientId, address)
                .enqueue(new Callback<Void>() {  // ← Tipo explícito: Callback<Void>
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d(TAG, "Resposta do createAddress - código: " + response.code());

                        if (response.isSuccessful()) {
                            Log.d(TAG, "Endereço salvo com sucesso");

                            // 2. Salva os FAMILIARES
                            Log.d(TAG, "Enviando " + (familyList != null ? familyList.size() : 0) + " familiares...");
                            api.createFamily(patientId, familyList)
                                    .enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            Log.d(TAG, "Resposta do createFamily - código: " + response.code());

                                            if (response.isSuccessful()) {
                                                Log.d(TAG, "Família salva com sucesso - finalizando com sucesso");
                                                success.postValue(true);
                                            } else {
                                                String errBody = getErrorBody(response);
                                                Log.e(TAG, "Erro ao salvar família - código " + response.code() + " - " + errBody);
                                                error.postValue("Erro ao salvar familiares: " + errBody);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            Log.e(TAG, "Falha de rede ao salvar família", t);
                                            error.postValue("Falha de conexão ao salvar familiares: " + t.getMessage());
                                        }
                                    });
                        } else {
                            String errBody = getErrorBody(response);
                            Log.e(TAG, "Erro ao salvar endereço - código " + response.code() + " - " + errBody);
                            error.postValue("Erro ao salvar endereço: " + errBody);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e(TAG, "Falha de rede ao salvar endereço", t);
                        error.postValue("Falha de conexão ao salvar endereço: " + t.getMessage());
                    }
                });
    }

    // Método auxiliar para ler corpo de erro da resposta
    private String getErrorBody(Response<?> response) {
        try (ResponseBody body = response.errorBody()) {
            return body != null ? body.string() : "sem detalhes";
        } catch (IOException e) {
            return "falha ao ler erro: " + e.getMessage();
        }
    }
}