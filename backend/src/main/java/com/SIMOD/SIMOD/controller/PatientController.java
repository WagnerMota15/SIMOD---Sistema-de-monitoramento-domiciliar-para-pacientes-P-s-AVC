package com.SIMOD.SIMOD.controller;

import com.SIMOD.SIMOD.config.UserDetailsImpl;
import com.SIMOD.SIMOD.domain.model.usuario.User;
import com.SIMOD.SIMOD.dto.vinculo.SolicitarVinculoRequest;
import com.SIMOD.SIMOD.services.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @PostMapping("/solicitar-cuidador")
    public ResponseEntity<Void> solicitarCuidador(
            Authentication authentication,
            @RequestBody @Valid SolicitarVinculoRequest request
    ) {
        patientService.solicitarVinculoCuidador(authentication, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/solicitar-profissional")
    public ResponseEntity<Void> solicitarProfissional(
            Authentication authentication,
            @RequestBody @Valid SolicitarVinculoRequest request
    ) {
        patientService.solicitarVinculoProfissional(authentication, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/aceitar-cuidador/{caregiverId}")
    public ResponseEntity<Void> aceitarCuidador(
            Authentication authentication,
            @PathVariable UUID caregiverId
    ) {
        patientService.aceitarSolicitacaoCuidador(authentication, caregiverId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rejeitar-cuidador/{caregiverId}")
    public ResponseEntity<Void> rejeitarCuidador(
            Authentication authentication,
            @PathVariable UUID caregiverId,
            @RequestBody(required = false) Map<String, String> body
    ) {
        String motivo = body != null ? body.get("motivo") : null;
        patientService.rejeitarSolicitacaoCuidador(authentication, caregiverId, motivo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/aceitar-profissional/{professionalId}")
    public ResponseEntity<Void> aceitarProfissional(
            Authentication authentication,
            @PathVariable UUID professionalId
    ) {
        patientService.aceitarSolicitacaoProfissional(authentication, professionalId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rejeitar-profissional/{professionalId}")
    public ResponseEntity<Void> rejeitarProfissional(
            Authentication authentication,
            @PathVariable UUID professionalId,
            @RequestBody(required = false) Map<String, String> body
    ) {
        String motivo = body != null ? body.get("motivo") : null;
        patientService.rejeitarSolicitacaoProfissional(authentication, professionalId, motivo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cuidadores-ativos")
    public ResponseEntity<List<SolicitarVinculoRequest.VinculoResponse>> listarCuidadoresAtivos(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                patientService.listarCuidadoresAtivos(authentication)
        );
    }

    @GetMapping("/profissionais-ativos")
    public ResponseEntity<List<SolicitarVinculoRequest.VinculoResponse>> listarProfissionaisAtivos(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                patientService.listarProfissionaisAtivos(authentication)
        );
    }

    @GetMapping("/solicitacoes-pendentes")
    public ResponseEntity<Map<String, List<SolicitarVinculoRequest.VinculoResponse>>> listarSolicitacoesPendentes(
            Authentication authentication
    ) {
        Map<String, List<SolicitarVinculoRequest.VinculoResponse>> response = new HashMap<>();
        response.put(
                "cuidadores",
                patientService.listarSolicitacoesPendentesCuidadores(authentication)
        );
        response.put(
                "profissionais",
                patientService.listarSolicitacoesPendentesProfissionais(authentication)
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/desvincular-cuidador/{caregiverId}")
    public ResponseEntity<Void> desfazerVinculoCuidador(
            Authentication authentication,
            @PathVariable UUID caregiverId
    ) {
        patientService.desfazerVinculoCuidador(authentication, caregiverId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/desvincular-profissional/{professionalId}")
    public ResponseEntity<Void> desfazerVinculoProfissional(
            Authentication authentication,
            @PathVariable UUID professionalId
    ) {
        patientService.desfazerVinculoProfissional(authentication, professionalId);
        return ResponseEntity.noContent().build();
    }

    private UUID getPatientId(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getUser().getIdUser();
    }
}
