package com.SIMOD.SIMOD.controller;

import com.SIMOD.SIMOD.dto.endereco.AddressRequest;
import com.SIMOD.SIMOD.dto.endereco.AddressResponse;
import com.SIMOD.SIMOD.services.PatientAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/patients/{patientId}/addresses")
@RequiredArgsConstructor
public class PatientAddressController {

    public final PatientAddressService patientAddressService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AddressResponse addAddress(@PathVariable UUID patientId, @RequestBody AddressRequest request) {
        return patientAddressService.addAddressToPatient(patientId, request);
    }

}
