package com.example.simodapp.data.repository;

import com.example.simodapp.data.api.AddressApi;
import com.example.simodapp.data.dto.AddressRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddresRepository {
    private final AddressApi api;


    public AddresRepository(AddressApi api) {
        this.api = api;
    }

    public void saveAddress(
            AddressRequest request,
            Runnable onSuccess,
            java.util.function.Consumer<String> onError
    ) {
        api.saveAddress(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    onSuccess.run();
                } else {
                    onError.accept("ERRO AO SALVAR ENDEREÇO");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                onError.accept(t.getMessage());
            }
        });
    }

}
