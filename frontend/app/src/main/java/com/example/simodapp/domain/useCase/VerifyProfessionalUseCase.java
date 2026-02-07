package com.example.simodapp.domain.useCase;


import com.example.simodapp.data.repository.ProfessionalRepository;
import com.example.simodapp.domain.result.VerificationResult;

public class VerifyProfessionalUseCase {

    private final ProfessionalRepository repository;

    public VerifyProfessionalUseCase(ProfessionalRepository repository) {
        this.repository = repository;
    }

    public VerificationResult execute(
            String cpf,
            String council,
            String registration,
            String uf
    ) {
        boolean valid = repository.verifyProfessional(
                cpf, council, registration, uf
        );

        if (valid) {
            return VerificationResult.approved();
        }

        return VerificationResult.rejected(
                "Profissional não encontrado ou inativo"
        );
    }
}