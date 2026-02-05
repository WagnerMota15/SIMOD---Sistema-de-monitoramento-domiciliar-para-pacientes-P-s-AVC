package com.SIMOD.SIMOD.controller;

import com.SIMOD.SIMOD.dto.endereco.AddressRequest;
import com.SIMOD.SIMOD.dto.familia.FamilyRequest;
import com.SIMOD.SIMOD.dto.familia.FamilyResponse;
import com.SIMOD.SIMOD.services.FamilyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auth/patients/{patientId}/family")
@RequiredArgsConstructor
public class FamilyController {

    public final FamilyService familyService;

    @PostMapping("/batch")
    public ResponseEntity<Void> createBatch(
            @PathVariable UUID patientId,
            @RequestBody @Valid List<FamilyRequest> requests
    ) {
        familyService.createContacstFamily(patientId, requests);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
