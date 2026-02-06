package com.example.simodapp.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.simodapp.data.api.AuthApi;
import com.example.simodapp.data.repository.AddressFamilyRepository;
import com.example.simodapp.util.SessionManager;
import com.example.simodapp.data.api.RetrofitClient;

public class AddressFamilyViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public AddressFamilyViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        // 1️⃣ Session
        SessionManager sessionManager = new SessionManager(context);

        // 2️⃣ Retrofit com token
        AuthApi authApi =
                RetrofitClient
                        .getClient(sessionManager)
                        .create(AuthApi.class);

        // 3️⃣ Repository
        AddressFamilyRepository repository =
                new AddressFamilyRepository(authApi);

        // 4️⃣ ViewModel
        return (T) new AddressFamilyViewModel(repository);
    }
}
