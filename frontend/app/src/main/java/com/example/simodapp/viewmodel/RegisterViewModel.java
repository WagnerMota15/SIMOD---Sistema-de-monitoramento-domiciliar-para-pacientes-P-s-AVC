package com.example.simodapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.simodapp.data.api.AuthApi;
import com.example.simodapp.data.api.RetrofitClient;
import com.example.simodapp.data.dto.RegisterResponse;
import com.example.simodapp.data.repository.AuthRepository;
import com.example.simodapp.data.repository.RegisterCallback;
import com.example.simodapp.domain.enums.Role;
import com.example.simodapp.domain.enums.StrokeTypes;
import com.example.simodapp.util.SessionManager;

public class RegisterViewModel extends ViewModel {

    private final AuthRepository authRepository;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<String> userId = new MutableLiveData<>();

    public RegisterViewModel(SessionManager sessionManager) {
        AuthApi authApi = RetrofitClient.getClient(sessionManager).create(AuthApi.class);
        this.authRepository = new AuthRepository(authApi);
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<String> getUserId() {
        return userId;
    }

    public void register(String nameComplete, String cpf, String email, String password, String telephone, Role role, StrokeTypes strokeTypes, String numCouncil){
        loading.setValue(true);

        authRepository.register(nameComplete, cpf, email, password, telephone, role, strokeTypes, numCouncil, new RegisterCallback() {
            @Override
            public void registerSucess(RegisterResponse response) {
                loading.postValue(false);
                userId.postValue(response.getUserId());
            }

            @Override
            public void registerError(String message) {
                loading.postValue(false);
                error.postValue(message);
            }
        });
    }

}
