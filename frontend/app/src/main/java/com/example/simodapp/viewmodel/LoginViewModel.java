package com.example.simodapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.simodapp.data.repository.LoginCallback;
import com.example.simodapp.data.repository.AuthRepository;
import com.example.simodapp.data.dto.LoginResponse;

public class LoginViewModel extends ViewModel {


    private final AuthRepository authRepository;
    private final MutableLiveData<LoginResponse> loginResult = new MutableLiveData<>();

    public LoginViewModel() {
        authRepository = new AuthRepository();
    }

    public LiveData<LoginResponse> getLoginResult(){
        return loginResult;
    }

    public void login(String login,String password){

        authRepository.login(login, password, new LoginCallback() {
            @Override
            public void onSucess(LoginResponse response) {
                Log.d("LOGIN","Token: "+response.getToken());
            }

            @Override
            public void onError(String message) {
                Log.d("LOGIN",message);
            }
        });

    }

}
