package com.example.simodapp.data.repository;


import com.example.simodapp.data.dataSource.ProfessionalCsvDataSource;

public class ProfessionalRepositoryImpl implements ProfessionalRepository {

    private final ProfessionalCsvDataSource dataSource;

    public ProfessionalRepositoryImpl(ProfessionalCsvDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean verifyProfessional(
            String cpf,
            String council,
            String registration,
            String uf
    ) {
        return dataSource.verify(cpf, council, registration, uf);
    }
}
