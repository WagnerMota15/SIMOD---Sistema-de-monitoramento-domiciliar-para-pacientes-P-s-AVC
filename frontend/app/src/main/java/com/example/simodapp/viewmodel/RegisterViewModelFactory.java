package com.example.simodapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.simodapp.data.repository.AuthRepository;

public class RegisterViewModelFactory implements ViewModelProvider.Factory {

    private final AuthRepository repository;

    public RegisterViewModelFactory(AuthRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RegisterViewModel.class)) {
            return (T) new RegisterViewModel(repository);
        }
        throw new IllegalArgumentException("ViewModel desconhecido");
    }
}
