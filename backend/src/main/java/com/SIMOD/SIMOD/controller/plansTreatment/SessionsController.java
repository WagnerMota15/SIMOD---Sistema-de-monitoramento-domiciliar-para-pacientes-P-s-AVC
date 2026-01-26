package com.SIMOD.SIMOD.controller.plansTreatment;

import com.SIMOD.SIMOD.domain.enums.SessionsStatus;
import com.SIMOD.SIMOD.domain.model.sessoes.Sessions;
import com.SIMOD.SIMOD.dto.plansTreatment.SessionsRequest;
import com.SIMOD.SIMOD.dto.plansTreatment.SessionsResponse;
import com.SIMOD.SIMOD.services.CaregiverService;
import com.SIMOD.SIMOD.services.PatientService;
import com.SIMOD.SIMOD.services.ProfessionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

@RestController
@RequestMapping("/sessoes")
@RequiredArgsConstructor
public class SessionsController {
    private final ProfessionalService professionalService;
    private final PatientService patientService;
    private final CaregiverService caregiverService;

    // ========== Profissionais ==========
    @PostMapping("/profissionais/marcar-sessao/{patientId}")
    public ResponseEntity<Sessions> marcarSessaoProfissional(
            Authentication authentication,
            @PathVariable UUID patientId,
            @Valid @RequestBody SessionsRequest request) {

        Sessions session = professionalService.marcarSessao(authentication, patientId, request);
        return ResponseEntity.ok(session);
    }

    @DeleteMapping("/profissionais/{sessaoId}")
    public ResponseEntity<Void> desmarcarSessaoProfissionais(
            Authentication authentication,
            @PathVariable UUID sessaoId) {

        professionalService.desmarcarSessao(authentication, sessaoId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/profissionais/{sessaoId}/confirmar")
    public ResponseEntity<Sessions> confirmarSessaoProfissionais(
            Authentication authentication,
            @PathVariable UUID sessaoId) {

        Sessions session = professionalService.confirmarSessao(authentication, sessaoId);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/profissionais/{sessaoId}/rejeitar")
    public ResponseEntity<Sessions> rejeitarSessaoProfissionais(
            Authentication authentication,
            @PathVariable UUID sessaoId,
            @RequestBody String motivo) {

        Sessions session = professionalService.rejeitarSessao(authentication, sessaoId, motivo);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/profissionais/{sessaoId}/cancelar")
    public ResponseEntity<Void> cancelarSessaoProfissionais(
            Authentication authentication,
            @PathVariable UUID sessaoId,
            @RequestBody String motivo) {

        professionalService.cancelarSessao(authentication, sessaoId, motivo);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/profissionais/{sessaoId}/reagendar")
    public ResponseEntity<Sessions> reagendarSessaoProfissionais(
            Authentication authentication,
            @PathVariable UUID sessaoId,
            @Valid @RequestBody SessionsRequest request) {

        Sessions session = professionalService.reagendarSessao(authentication, sessaoId, request.dateTime(), request);
        return ResponseEntity.ok(session);
    }

    @GetMapping("/profissionais/todas-sessoes")
    public ResponseEntity<Page<SessionsResponse>> listarTodasSessoesProfissionais(
            Authentication authentication,
            @RequestParam(required = false) SessionsStatus status,
            @PageableDefault(size = 10, sort = "dateTime", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<SessionsResponse> page = professionalService.listarTodasMinhasSessoes(authentication, status, pageable);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/profissionais/sessoes-anteriores")
    public ResponseEntity<Page<SessionsResponse>> listarSessoesAnterioresProfissionais(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "dateTime", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<SessionsResponse> page = professionalService.listarSessoesAnteriores(authentication, pageable);
        return ResponseEntity.ok(page);
    }


    // ========== Cuidadroes ==========
    @PostMapping("/cuidadores/{patientId}/marcar-sessao/{professionalId}")
    public ResponseEntity<Sessions> marcarSessaoCuidadorCuidadores(
            Authentication authentication,
            @PathVariable UUID patientId,
            @PathVariable UUID professionalId,
            @Valid @RequestBody SessionsRequest request) {

        Sessions session = caregiverService.marcarSessaoParaPaciente(authentication, patientId, professionalId, request);
        return ResponseEntity.ok(session);
    }

    @DeleteMapping("/cuidadores/{sessaoId}")
    public ResponseEntity<Void> desmarcarSessaoCuidadores(
            Authentication authentication,
            @PathVariable UUID sessaoId) {

        caregiverService.desmarcarSessao(authentication, sessaoId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/cuidadores/{sessaoId}/confirmar")
    public ResponseEntity<Sessions> confirmarSessaoCuidadores(
            Authentication authentication,
            @PathVariable UUID sessaoId) {

        Sessions session = caregiverService.confirmarSessao(authentication, sessaoId);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/cuidadores/{sessaoId}/rejeitar")
    public ResponseEntity<Sessions> rejeitarSessaoCuidadores(
            Authentication authentication,
            @PathVariable UUID sessaoId,
            @RequestBody String motivo) {

        Sessions session = caregiverService.rejeitarSessao(authentication, sessaoId, motivo);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/cuidadores/{sessaoId}/cancelar")
    public ResponseEntity<Void> cancelarSessaoCuidadores(
            Authentication authentication,
            @PathVariable UUID sessaoId,
            @RequestBody String motivo) {

        caregiverService.cancelarSessao(authentication, sessaoId, motivo);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/cuidadores/{sessaoId}/reagendar")
    public ResponseEntity<Sessions> reagendarSessaoCuidadores(
            Authentication authentication,
            @PathVariable UUID sessaoId,
            @Valid @RequestBody SessionsRequest request) {

        Sessions session = caregiverService.reagendarSessao(authentication, sessaoId, request.dateTime(), request);
        return ResponseEntity.ok(session);
    }

    @GetMapping("/cuidadores/todas-sessoes/{patientId}")
    public ResponseEntity<Page<SessionsResponse>> listarTodasSessoesCuidadores(
            Authentication authentication,
            UUID patientId,
            @RequestParam(required = false) SessionsStatus status,
            @PageableDefault(size = 10, sort = "dateTime", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<SessionsResponse> page = caregiverService.listarTodasSessoesDoPaciente(authentication, patientId, status, pageable);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/cuidadores/sessoes-anteriores{patientId}")
    public ResponseEntity<Page<SessionsResponse>> listarSessoesAnterioresCuidadores(
            Authentication authentication,
            UUID patientId,
            @PageableDefault(size = 10, sort = "dateTime", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<SessionsResponse> page = caregiverService.listarSessoesAnterioresDoPaciente(authentication, patientId, pageable);
        return ResponseEntity.ok(page);
    }


    // ========== Paciente ==========
    @PostMapping("/pacientes/marcar-sessao/{professionalId}")
    public ResponseEntity<Sessions> marcarSessaoPaciente(
            Authentication authentication,
            @PathVariable UUID professionalId,
            @Valid @RequestBody SessionsRequest request) {

        Sessions session = patientService.marcarSessaoParaPaciente(authentication, professionalId, request);
        return ResponseEntity.ok(session);
    }

    @DeleteMapping("/pacientes/{sessaoId}")
    public ResponseEntity<Void> desmarcarSessaoPaciente(
            Authentication authentication,
            @PathVariable UUID sessaoId) {

        patientService.desmarcarSessao(authentication, sessaoId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/pacientes/{sessaoId}/confirmar")
    public ResponseEntity<Sessions> confirmarSessaoPaciente(
            Authentication authentication,
            @PathVariable UUID sessaoId) {

        Sessions session = patientService.confirmarSessao(authentication, sessaoId);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/pacientes/{sessaoId}/rejeitar")
    public ResponseEntity<Sessions> rejeitarSessaoPaciente(
            Authentication authentication,
            @PathVariable UUID sessaoId,
            @RequestBody String motivo) {

        Sessions session = patientService.rejeitarSessao(authentication, sessaoId, motivo);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/pacientes/{sessaoId}/cancelar")
    public ResponseEntity<Void> cancelarSessaoPaciente(
            Authentication authentication,
            @PathVariable UUID sessaoId,
            @RequestBody String motivo) {

        patientService.cancelarSessao(authentication, sessaoId, motivo);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/pacientes/{sessaoId}/reagendar")
    public ResponseEntity<Sessions> reagendarSessaoPaciente(
            Authentication authentication,
            @PathVariable UUID sessaoId,
            @Valid @RequestBody SessionsRequest request) {

        Sessions session = patientService.reagendarSessao(authentication, sessaoId, request.dateTime(), request);
        return ResponseEntity.ok(session);
    }

    @GetMapping("/pacientes/todas-sessoes")
    public ResponseEntity<Page<SessionsResponse>> listarTodasSessoesPaciente(
            Authentication authentication,
            @RequestParam(required = false) SessionsStatus status,
            @PageableDefault(size = 10, sort = "dateTime", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<SessionsResponse> page = patientService.listarTodasMinhasSessoes(authentication, status, pageable);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/pacientes/sessoes-anteriores")
    public ResponseEntity<Page<SessionsResponse>> listarSessoesAnterioresPaciente(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "dateTime", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<SessionsResponse> page = patientService.listarSessoesAnteriores(authentication, pageable);
        return ResponseEntity.ok(page);
    }
}

