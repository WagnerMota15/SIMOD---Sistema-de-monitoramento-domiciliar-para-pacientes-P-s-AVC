package com.SIMOD.SIMOD.controller;

import com.SIMOD.SIMOD.dto.vinculo.SolicitarVinculoRequest;
import com.SIMOD.SIMOD.services.ProfessionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/profissionais")
@RequiredArgsConstructor
public class ProfissionalSaudeController {

    private final ProfessionalService professionalService;

    // Profissional solicita vínculo com paciente (por CPF)
    @PostMapping("/{professionalId}/solicitar-paciente")
    public ResponseEntity<Void> solicitarPaciente(
            @PathVariable UUID professionalId,
            @RequestBody @Valid SolicitarVinculoRequest request) {
        professionalService.solicitarVinculoPaciente(professionalId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Profissional aceita solicitação de paciente
    @PostMapping("/{professionalId}/aceitar-paciente/{patientId}")
    public ResponseEntity<Void> aceitarPaciente(
            @PathVariable UUID professionalId,
            @PathVariable UUID patientId) {
        professionalService.aceitarSolicitacaoPaciente(professionalId, patientId);
        return ResponseEntity.ok().build();
    }

    // Profissional rejeita solicitação de paciente
    @PostMapping("/{professionalId}/rejeitar-paciente/{patientId}")
    public ResponseEntity<Void> rejeitarPaciente(
            @PathVariable UUID professionalId,
            @PathVariable UUID patientId,
            @RequestBody Map<String, String> body) {
        String motivo = body.getOrDefault("motivo", null);
        professionalService.rejeitarSolicitacaoPaciente(professionalId, patientId, motivo);
        return ResponseEntity.ok().build();
    }

    // Lista pacientes ativos vinculados ao profissional
    @GetMapping("/{professionalId}/pacientes-ativos")
    public ResponseEntity<List<SolicitarVinculoRequest.VinculoResponse>> listarPacientesAtivos(@PathVariable UUID professionalId) {
        return ResponseEntity.ok(professionalService.listarPacientesAtivos(professionalId));
    }

    // Lista solicitações pendentes de pacientes para este profissional
    @GetMapping("/{professionalId}/solicitacoes-pendentes")
    public ResponseEntity<List<SolicitarVinculoRequest.VinculoResponse>> listarSolicitacoesPendentes(@PathVariable UUID professionalId) {
        return ResponseEntity.ok(professionalService.listarSolicitacoesPendentesPacientes(professionalId));
    }
}