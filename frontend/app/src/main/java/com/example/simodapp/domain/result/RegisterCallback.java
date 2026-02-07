package com.example.simodapp.domain.result;

import com.example.simodapp.data.dto.RegisterResponse;

public interface RegisterCallback {
    void registerSucess(RegisterResponse response);
    void registerError(String message);
}
