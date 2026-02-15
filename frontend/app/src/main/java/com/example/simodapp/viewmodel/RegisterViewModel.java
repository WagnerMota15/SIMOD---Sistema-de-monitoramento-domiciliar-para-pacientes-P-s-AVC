package com.example.simodapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.simodapp.data.dto.RegisterRequest;
import com.example.simodapp.data.dto.RegisterResponse;
import com.example.simodapp.data.repository.AuthRepository;
import com.example.simodapp.domain.result.RegisterCallback;
import com.example.simodapp.domain.enums.Role;
import com.example.simodapp.domain.enums.StrokeTypes;

public class RegisterViewModel extends ViewModel {

    private final AuthRepository authRepository;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<RegisterResponse> registerSuccess = new MutableLiveData<>();

    public RegisterViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<RegisterResponse> getRegisterSuccess() {
        return registerSuccess;
    }

    public void register(
            String nameComplete,
            String cpf,
            String email,
            String password,
            String telephone,
            Role role,
            StrokeTypes strokeTypes,
            String numCouncil
    ) {

        loading.setValue(true);

        RegisterRequest request = new RegisterRequest(
                nameComplete,
                cpf,
                email,
                password,
                telephone,
                role,
                strokeTypes,
                numCouncil
        );

        authRepository.register(request, new RegisterCallback() {
            @Override
            public void registerSucess(RegisterResponse response) {
                loading.postValue(false);
                registerSuccess.postValue(response);
            }

            @Override
            public void registerError(String message) {
                loading.postValue(false);
                error.postValue(message);
            }
        });
    }
}
