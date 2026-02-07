package com.example.simodapp.viewmodel;

import android.content.Context;

import androidx.lifecycle.ViewModelProvider;

import com.example.simodapp.data.api.AuthApi;
import com.example.simodapp.data.api.RetrofitClient;
import com.example.simodapp.data.dataSource.ProfessionalCsvDataSource;
import com.example.simodapp.data.repository.AuthRepository;
import com.example.simodapp.data.repository.ProfessionalRepositoryImpl;
import com.example.simodapp.domain.useCase.VerifyProfessionalUseCase;
import com.example.simodapp.util.SessionManager;


public class ViewModelFactory {

    public static FinalProfessionalViewModel provideFinalProfessional(Context context) {

        ProfessionalCsvDataSource dataSource =
                new ProfessionalCsvDataSource(context);

        ProfessionalRepositoryImpl repository =
                new ProfessionalRepositoryImpl(dataSource);

        VerifyProfessionalUseCase useCase =
                new VerifyProfessionalUseCase(repository);

        return new FinalProfessionalViewModel(useCase);
    }

    public static RegisterViewModel provideRegister(Context context) {

        SessionManager sessionManager =
                new SessionManager(context);

        AuthApi authApi =
                RetrofitClient
                        .getClient(sessionManager)
                        .create(AuthApi.class);

        AuthRepository authRepository =
                new AuthRepository(authApi, sessionManager);

        RegisterViewModelFactory factory =
                new RegisterViewModelFactory(authRepository);

        return new ViewModelProvider(
                (androidx.lifecycle.ViewModelStoreOwner) context,
                factory
        ).get(RegisterViewModel.class);
    }
}
