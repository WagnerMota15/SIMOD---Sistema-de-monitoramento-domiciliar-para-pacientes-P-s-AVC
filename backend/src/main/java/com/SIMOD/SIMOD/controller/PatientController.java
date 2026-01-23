package com.SIMOD.SIMOD.controller;

import com.SIMOD.SIMOD.dto.vinculo.SolicitarVinculoRequest;
import com.SIMOD.SIMOD.services.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping("/{patientId}/solicitar-cuidador")
    public ResponseEntity<Void> solicitarCuidador(
            @PathVariable UUID patientId,
            @RequestBody @Valid SolicitarVinculoRequest request) {
        patientService.solicitarVinculoCuidador(patientId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{patientId}/solicitar-profissional")
    public ResponseEntity<Void> solicitarProfissional(
            @PathVariable UUID patientId,
            @RequestBody @Valid SolicitarVinculoRequest request) {
        patientService.solicitarVinculoProfissional(patientId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{patientId}/aceitar-cuidador/{caregiverId}")
    public ResponseEntity<Void> aceitarCuidador(
            @PathVariable UUID patientId,
            @PathVariable UUID caregiverId) {
        patientService.aceitarSolicitacaoCuidador(patientId, caregiverId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{patientId}/rejeitar-cuidador/{caregiverId}")
    public ResponseEntity<Void> rejeitarCuidador(
            @PathVariable UUID patientId,
            @PathVariable UUID caregiverId,
            @RequestBody Map<String, String> body) {  // motivo é opcional
        String motivo = body.getOrDefault("motivo", null);
        patientService.rejeitarSolicitacaoCuidador(patientId, caregiverId, motivo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{patientId}/aceitar-profissional/{professionalId}")
    public ResponseEntity<Void> aceitarProfissional(
            @PathVariable UUID patientId,
            @PathVariable UUID professionalId) {
        patientService.aceitarSolicitacaoProfissional(patientId, professionalId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{patientId}/rejeitar-profissional/{professionalId}")
    public ResponseEntity<Void> rejeitarProfissional(
            @PathVariable UUID patientId,
            @PathVariable UUID professionalId,
            @RequestBody Map<String, String> body) {  // motivo é opcional
        String motivo = body.getOrDefault("motivo", null);
        patientService.rejeitarSolicitacaoProfissional(patientId, professionalId, motivo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{patientId}/cuidadores-ativos")
    public ResponseEntity<List<SolicitarVinculoRequest.VinculoResponse>> listarCuidadoresAtivos(@PathVariable UUID patientId) {
        return ResponseEntity.ok(patientService.listarCuidadoresAtivos(patientId));
    }

    @GetMapping("/{patientId}/profissionais-ativos")
    public ResponseEntity<List<SolicitarVinculoRequest.VinculoResponse>> listarProfissionaisAtivos(@PathVariable UUID patientId) {
        return ResponseEntity.ok(patientService.listarProfissionaisAtivos(patientId));
    }

    @GetMapping("/{patientId}/solicitacoes-pendentes-cuidadores")
    public ResponseEntity<List<SolicitarVinculoRequest.VinculoResponse>> listarSolicitacoesPendentesCuidadores(@PathVariable UUID patientId) {
        return ResponseEntity.ok(patientService.listarSolicitacoesPendentesCuidadores(patientId));
    }

    @GetMapping("/{patientId}/solicitacoes-pendentes-profissionais")
    public ResponseEntity<List<SolicitarVinculoRequest.VinculoResponse>> listarSolicitacoesPendentesProfissionais(@PathVariable UUID patientId) {
        return ResponseEntity.ok(patientService.listarSolicitacoesPendentesProfissionais(patientId));
    }

    @GetMapping("/{patientId}/solicitacoes-pendentes")
    public ResponseEntity<Map<String, List<SolicitarVinculoRequest.VinculoResponse>>> listarTodasSolicitacoesPendentes(@PathVariable UUID patientId) {
        Map<String, List<SolicitarVinculoRequest.VinculoResponse>> response = new HashMap<>();
        response.put("cuidadores", patientService.listarSolicitacoesPendentesCuidadores(patientId));
        response.put("profissionais", patientService.listarSolicitacoesPendentesProfissionais(patientId));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{patientId}/desvincular-profissionais/{professionalId}")
    public ResponseEntity<Void> desfazerVinculoProfissional(
            @PathVariable UUID patientId,
            @PathVariable UUID professionalId
    ) {
        patientService.desfazerVinculoProfissional(patientId, professionalId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{patientId}/desvincular-cuidador/{cuidadorId}")
    public ResponseEntity<Void> desfazerVinculoCuidador(
            @PathVariable UUID patientId,
            @PathVariable UUID caregiverId
            ) {
        patientService.desfazerVinculoCuidador(patientId, caregiverId);
        return ResponseEntity.noContent().build();
    }
}