package com.SIMOD.SIMOD.controller;

import com.SIMOD.SIMOD.dto.vinculo.SolicitarVinculoRequest;
import com.SIMOD.SIMOD.services.CaregiverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/cuidadores")
@RequiredArgsConstructor
public class CaregiverController {

    private final CaregiverService caregiverService;

    @PostMapping("/{caregiverId}/solicitar-paciente")
    public ResponseEntity<Void> solicitarPaciente(
            @PathVariable UUID caregiverId,
            @RequestBody @Valid SolicitarVinculoRequest request) {
        caregiverService.solicitarVinculoPaciente(caregiverId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{caregiverId}/aceitar-paciente/{patientId}")
    public ResponseEntity<Void> aceitarPaciente(
            @PathVariable UUID caregiverId,
            @PathVariable UUID patientId) {
        caregiverService.aceitarSolicitacaoPaciente(caregiverId, patientId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{caregiverId}/rejeitar-paciente/{patientId}")
    public ResponseEntity<Void> rejeitarPaciente(
            @PathVariable UUID caregiverId,
            @PathVariable UUID patientId,
            @RequestBody Map<String, String> body) {
        String motivo = body.getOrDefault("motivo", null);
        caregiverService.rejeitarSolicitacaoPaciente(caregiverId, patientId, motivo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{caregiverId}/pacientes-ativos")
    public ResponseEntity<List<SolicitarVinculoRequest.VinculoResponse>> listarPacientesAtivos(@PathVariable UUID caregiverId) {
        return ResponseEntity.ok(caregiverService.listarPacientesAtivos(caregiverId));
    }

    @GetMapping("/{caregiverId}/solicitacoes-pendentes")
    public ResponseEntity<List<SolicitarVinculoRequest.VinculoResponse>> listarSolicitacoesPendentes(@PathVariable UUID caregiverId) {
        return ResponseEntity.ok(caregiverService.listarSolicitacoesPendentesPacientes(caregiverId));
    }

    @DeleteMapping("/{caregiverId}/desvincular/{patientId}")
    public ResponseEntity<Void> desfazerVinculo(
            @PathVariable UUID caregiverId,
            @PathVariable UUID patientId
    ) {
        caregiverService.desfazerVinculoPaciente(caregiverId, patientId);
        return ResponseEntity.noContent().build();
    }
}