package com.example.simodapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.simodapp.domain.result.VerificationResult;
import com.example.simodapp.domain.state.FinalProfessionalUiState;
import com.example.simodapp.domain.useCase.VerifyProfessionalUseCase;

import java.util.concurrent.Executors;

public class FinalProfessionalViewModel extends ViewModel {

    private final VerifyProfessionalUseCase verifyProfessionalUseCase;

    private final MutableLiveData<FinalProfessionalUiState> uiState =
            new MutableLiveData<>(new FinalProfessionalUiState.Idle());

    public FinalProfessionalViewModel(
            VerifyProfessionalUseCase verifyProfessionalUseCase
    ) {
        this.verifyProfessionalUseCase = verifyProfessionalUseCase;
    }

    public LiveData<FinalProfessionalUiState> getUiState() {
        return uiState;
    }

    public void verifyProfessional(
            String cpf,
            String council,
            String registration,
            String uf
    ) {

        uiState.setValue(new FinalProfessionalUiState.Loading());

        Executors.newSingleThreadExecutor().execute(() -> {

            VerificationResult result =
                    verifyProfessionalUseCase.execute(
                            cpf, council, registration, uf
                    );

            if (result.isApproved()) {
                uiState.postValue(new FinalProfessionalUiState.Approved());
            } else {
                uiState.postValue(
                        new FinalProfessionalUiState.Rejected(
                                result.getMessage()
                        )
                );
            }
        });
    }
}