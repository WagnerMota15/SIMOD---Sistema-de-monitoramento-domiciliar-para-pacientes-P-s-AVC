package com.example.simodapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.simodapp.data.dto.LoginRequest;
import com.example.simodapp.data.dto.LoginResponse;
import com.example.simodapp.data.repository.AuthRepository;
import com.example.simodapp.domain.result.LoginCallback;

public class LoginViewModel extends ViewModel {

    private final AuthRepository authRepository;

    private final MutableLiveData<String> token = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public LoginViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<String> getToken() {
        return token;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public void login(String login, String password) {

        loading.setValue(true);
        error.setValue(null);

        LoginRequest request = new LoginRequest(login, password);

        authRepository.login(request, new LoginCallback() {
            @Override
            public void onSucess(LoginResponse response) {
                loading.postValue(false);
                token.postValue(response.getToken());
            }

            @Override
            public void onError(String message) {
                loading.postValue(false);
                error.postValue(message);
            }
        });
    }
}
