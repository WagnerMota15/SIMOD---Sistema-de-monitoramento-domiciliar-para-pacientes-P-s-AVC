package com.SIMOD.SIMOD.controller;

import com.SIMOD.SIMOD.dto.diario.HealthDiaryRequest;
import com.SIMOD.SIMOD.dto.diario.HealthDiaryResponse;
import com.SIMOD.SIMOD.services.HealthDiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@RestController
@RequestMapping("/diario-de-saude")
@RequiredArgsConstructor
public class HealthDiaryController {
    private final HealthDiaryService healthDiaryService;

    @PostMapping("/registrarDiario/{patientId}")
    public ResponseEntity<HealthDiaryResponse> registrarDiario(
            Authentication authentication,
            @PathVariable UUID patientId,
            @RequestBody @Valid HealthDiaryRequest request) {

        HealthDiaryResponse response = healthDiaryService.registrarDiario(authentication, patientId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/listarDiarios/{patientId}")
    public ResponseEntity<Page<HealthDiaryResponse>> listarDiarios(
            Authentication authentication,
            @PathVariable UUID patientId,
            @PageableDefault(sort = "diaryDate", direction = DESC) Pageable pageable) {

        Page<HealthDiaryResponse> diaries = healthDiaryService.listarDiarios(authentication, patientId, pageable);
        return ResponseEntity.ok(diaries);
    }

    @GetMapping("/buscar-diario/{patientId}/date/{date}")
    public ResponseEntity<HealthDiaryResponse> buscarDiarioPorData(
            Authentication authentication,
            @PathVariable UUID patientId,
            @PathVariable @DateTimeFormat(iso = DATE) LocalDate date) {

        HealthDiaryResponse response = healthDiaryService.buscarDiario(authentication, patientId, date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/diario-hoje/{patientId}")
    public ResponseEntity<HealthDiaryResponse> obterDiarioDeHoje(
            Authentication authentication,
            @PathVariable UUID patientId) {

        HealthDiaryResponse response = healthDiaryService.buscarDiario(authentication, patientId, LocalDate.now());
        return ResponseEntity.ok(response);
    }
}
