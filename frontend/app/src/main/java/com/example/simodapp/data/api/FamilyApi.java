package com.example.simodapp.data.api;

import com.example.simodapp.data.dto.FamilyRequest;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FamilyApi {
    @POST("/auth/patients/{patientId}/family")
    Call<Void> createFamilyContacts(
            @Path("patientId") UUID patientId,
            @Body List<FamilyRequest> requests
    );

}
