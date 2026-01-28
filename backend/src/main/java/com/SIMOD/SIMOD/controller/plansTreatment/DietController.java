package com.SIMOD.SIMOD.controller.plansTreatment;

import com.SIMOD.SIMOD.domain.model.dieta.Diet;
import com.SIMOD.SIMOD.dto.plansTreatment.DietRequest;
import com.SIMOD.SIMOD.dto.plansTreatment.DietResponse;
import com.SIMOD.SIMOD.dto.plansTreatment.MedicinesResponse;
import com.SIMOD.SIMOD.services.DietService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/dietas")
@RequiredArgsConstructor
public class DietController {
    private final DietService dietService;

    // ====== NUTRICIONISTA ======

    @PostMapping("/nutricionistas/prescrever/{patientId}")
    public ResponseEntity<DietResponse> prescreverDieta(
            Authentication authentication,
            @PathVariable UUID patientId,
            @RequestBody DietRequest request
    ) {
        Diet diet = dietService.prescreverDieta(authentication, patientId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapToResponse(diet));
    }

    @PutMapping("/nutricionistas/editar/{dietId}")
    public ResponseEntity<DietResponse> editarDieta(
            Authentication authentication,
            @PathVariable UUID dietId,
            @RequestBody DietRequest request
    ) {
        DietResponse response =
                dietService.editarDieta(authentication, dietId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/nutricionista/inativar/{dietId}")
    public ResponseEntity<Void> inativarDieta(
            Authentication authentication,
            @PathVariable UUID dietId
    ) {
        dietService.inativarDieta(authentication, dietId);
        return ResponseEntity.noContent().build();
    }

    // ====== CONSULTAS ======

    @GetMapping("/usuarios/listar-dietas")
    public Page<DietResponse> listarDietas(
            Authentication authentication,
            Pageable pageable
    ) {
        return dietService.listarDietas(authentication, pageable);
    }

    @GetMapping("/usuarios/listar-dietas-ativas")
    public Page<DietResponse> listarDietasAtivas(
            Authentication authentication,
            Pageable pageable
    ) {
        return dietService.listarDietasAtivas(authentication, pageable);
    }

    @GetMapping("/usuarios/listar-dietas-inativas")
    public Page<DietResponse> listarDietasInativas(
            Authentication authentication,
            Pageable pageable
    ) {
        return dietService.listarDietasInativas(authentication, pageable);
    }

    @GetMapping("/usuarios/listar-dietas-paciente/{patientId}")
    public Page<DietResponse> listarPorPaciente(
            Authentication authentication,
            @PathVariable UUID patientId,
            Pageable pageable
    ) {
        return dietService.listarDietasPorPaciente(authentication, patientId, pageable);
    }

    @GetMapping("/usuarios/listar-medicamento/{dietId}")
    public DietResponse buscarDieta(
            Authentication authentication,
            @PathVariable UUID dietId
    ) {
        return dietService.buscarDieta(authentication, dietId);
    }

    private DietResponse mapToResponse(Diet diet) {
        return new DietResponse(
                diet.getIdDiet(),
                diet.getFreqMeal(),
                diet.getSchedules(),
                diet.getDescription(),
                diet.getPatient().getIdUser(),
                diet.getProfessional().getIdUser()
        );
    }
}
