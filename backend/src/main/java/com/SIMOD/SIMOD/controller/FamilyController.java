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

import java.util.UUID;

@RestController
@RequestMapping("/patients/{patientId}/family")
@RequiredArgsConstructor
public class FamilyController {

    public final FamilyService familyService;

    @PostMapping
    public ResponseEntity<Void> create(@PathVariable UUID patientId, @RequestBody @Valid FamilyRequest request){
        familyService.createContactFamily(patientId,request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
