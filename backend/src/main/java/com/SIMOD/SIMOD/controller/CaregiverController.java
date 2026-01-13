package com.SIMOD.SIMOD.controller;

import com.SIMOD.SIMOD.domain.cuidador.Caregiver;
import com.SIMOD.SIMOD.dto.RegisterCaregiverRequest;
import com.SIMOD.SIMOD.services.CaregiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dto")

public class CaregiverController {
    @Autowired
    private CaregiverService caregiverService;

    @PostMapping
    public ResponseEntity<Caregiver> create(@RequestBody RegisterCaregiverRequest body){
        Caregiver cuidador = this.caregiverService.criarCuidador(body);
        return ResponseEntity.ok(cuidador);

    }
}
