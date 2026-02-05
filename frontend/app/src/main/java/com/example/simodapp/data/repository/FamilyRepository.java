package com.example.simodapp.data.repository;

import com.example.simodapp.data.api.FamilyApi;
import com.example.simodapp.data.dto.FamilyRequest;
import com.example.simodapp.data.dto.FamilyResponse;

import java.util.List;
import java.util.UUID;

import retrofit2.Callback;

public class FamilyRepository {
    private final FamilyApi familyApi;

    public FamilyRepository(FamilyApi familyApi) {
        this.familyApi = familyApi;
    }

    // Enviar lista de contatos familiares
    public void createFamilyContacts(UUID patientId, List<FamilyRequest> requests, Callback<Void> callback) {
        familyApi.createFamilyContacts(patientId, requests).enqueue(callback);
    }

}
