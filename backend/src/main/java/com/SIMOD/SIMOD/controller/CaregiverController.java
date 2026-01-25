package com.SIMOD.SIMOD.controller;

import com.SIMOD.SIMOD.config.UserDetailsImpl;
import com.SIMOD.SIMOD.dto.vinculo.SolicitarVinculoRequest;
import com.SIMOD.SIMOD.services.CaregiverService;
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
@RequestMapping("/cuidadores")
@RequiredArgsConstructor
public class CaregiverController {

    private final CaregiverService caregiverService;

    @PostMapping("/solicitar-paciente")
    public ResponseEntity<Void> solicitarPaciente(
            @RequestBody @Valid SolicitarVinculoRequest request,
            Authentication authentication
    ) {
        caregiverService.solicitarVinculoPaciente(authentication, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/aceitar-paciente/{patientId}")
    public ResponseEntity<Void> aceitarPaciente(
            @PathVariable UUID patientId,
            Authentication authentication
    ) {
        caregiverService.aceitarSolicitacaoPaciente(authentication, patientId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rejeitar-paciente/{patientId}")
    public ResponseEntity<Void> rejeitarPaciente(
            @PathVariable UUID patientId,
            @RequestBody Map<String, String> body,
            Authentication authentication
    ) {
        caregiverService.rejeitarSolicitacaoPaciente(
                authentication,
                patientId,
                body.getOrDefault("motivo", null)
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pacientes-ativos")
    public ResponseEntity<List<SolicitarVinculoRequest.VinculoResponse>> listarPacientesAtivos(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                caregiverService.listarPacientesAtivos(authentication)
        );
    }

    @GetMapping("/solicitacoes-pendentes")
    public ResponseEntity<List<SolicitarVinculoRequest.VinculoResponse>> listarSolicitacoesPendentes(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                caregiverService.listarSolicitacoesPendentesPacientes(authentication)
        );
    }

    @DeleteMapping("/desvincular/{patientId}")
    public ResponseEntity<Void> desfazerVinculo(
            @PathVariable UUID patientId,
            Authentication authentication
    ) {
        caregiverService.desfazerVinculoPaciente(authentication, patientId);
        return ResponseEntity.noContent().build();
    }
}