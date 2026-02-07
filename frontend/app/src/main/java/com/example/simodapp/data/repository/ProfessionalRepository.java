package com.example.simodapp.data.repository;

public interface ProfessionalRepository {

    boolean verifyProfessional(
            String cpf,
            String council,
            String registration,
            String uf
    );
}
