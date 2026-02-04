package com.example.simodapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.simodapp.data.api.AuthApi;
import com.example.simodapp.data.api.RetrofitClient;
import com.example.simodapp.data.repository.LoginCallback;
import com.example.simodapp.data.repository.AuthRepository;
import com.example.simodapp.data.dto.LoginResponse;
import com.example.simodapp.util.SessionManager;

public class LoginViewModel extends ViewModel {


    private final AuthRepository authRepository;
    private final MutableLiveData<LoginResponse> loginSucess = new MutableLiveData<>();
    private final MutableLiveData<String> loginError = new MutableLiveData<>();

    public LoginViewModel(SessionManager sessionManager) {
        AuthApi authApi = RetrofitClient.getClient(sessionManager).create(AuthApi.class);
        this.authRepository = new AuthRepository(authApi);
    }

    public LiveData<LoginResponse> getLoginSucess(){

        return loginSucess;
    }

    public LiveData<String> getLoginError(){
        return loginError;
    }

    public void login(String login,String password){

        authRepository.login(login, password, new LoginCallback() {
            @Override
            public void onSucess(LoginResponse response) {
                loginSucess.postValue(response);
            }

            @Override
            public void onError(String message) {
                loginError.postValue(message);

            }
        });

    }

}
