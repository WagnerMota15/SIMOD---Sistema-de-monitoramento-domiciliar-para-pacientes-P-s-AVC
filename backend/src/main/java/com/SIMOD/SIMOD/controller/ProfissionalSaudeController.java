package com.SIMOD.SIMOD.controller;

import com.SIMOD.SIMOD.dto.vinculo.SolicitarVinculoRequest;
import com.SIMOD.SIMOD.services.ProfessionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/profissionais")
@RequiredArgsConstructor
public class ProfissionalSaudeController {

    private final ProfessionalService professionalService;

    @PostMapping("/solicitar-paciente")
    public ResponseEntity<Void> solicitarPaciente(
            Authentication authentication,
            @RequestBody @Valid SolicitarVinculoRequest request) {

        professionalService.solicitarVinculoPaciente(authentication, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/aceitar-paciente/{patientId}")
    public ResponseEntity<Void> aceitarPaciente(
            @PathVariable UUID patientId,
            Authentication authentication) {

        professionalService.aceitarSolicitacaoPaciente(authentication, patientId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rejeitar-paciente/{patientId}")
    public ResponseEntity<Void> rejeitarPaciente(
            @PathVariable UUID patientId,
            @RequestBody Map<String, String> body,
            Authentication authentication) {

        String motivo = body.getOrDefault("motivo", null);
        professionalService.rejeitarSolicitacaoPaciente(authentication, patientId, motivo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pacientes-ativos")
    public ResponseEntity<List<SolicitarVinculoRequest.VinculoResponse>> listarPacientesAtivos(
            Authentication authentication) {

        return ResponseEntity.ok(
                professionalService.listarPacientesAtivos(authentication)
        );
    }

    @GetMapping("/solicitacoes-pendentes")
    public ResponseEntity<List<SolicitarVinculoRequest.VinculoResponse>> listarSolicitacoesPendentes(
            Authentication authentication) {

        return ResponseEntity.ok(
                professionalService.listarSolicitacoesPendentesPacientes(authentication)
        );
    }

    @DeleteMapping("/desvincular/{patientId}")
    public ResponseEntity<Void> desfazerVinculo(
            @PathVariable UUID patientId,
            Authentication authentication) {

        professionalService.desfazerVinculoPaciente(authentication, patientId);
        return ResponseEntity.noContent().build();
    }
}