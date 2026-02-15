package com.example.simodapp.domain.result;

import com.example.simodapp.data.dto.LoginResponse;

public interface LoginCallback {
    void onSucess(LoginResponse response);
    void onError(String message);
}
