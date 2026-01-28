package com.SIMOD.SIMOD.controller.plansTreatment;

import com.SIMOD.SIMOD.domain.enums.SessionsStatus;
import com.SIMOD.SIMOD.domain.enums.Status;
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
    public ResponseEntity<SessionsResponse> marcarSessaoProfissional(
            Authentication authentication,
            @PathVariable UUID patientId,
            @Valid @RequestBody SessionsRequest request) {

        Sessions session = professionalService.marcarSessao(authentication, patientId, request);

        SessionsResponse response = mapearParaResponse(session);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/profissionais/{sessaoId}/desmarcar")
    public ResponseEntity<Void> desmarcarSessaoProfissionais(
            Authentication authentication,
            @PathVariable UUID sessaoId) {

        professionalService.desmarcarSessao(authentication, sessaoId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/profissionais/{sessaoId}/confirmar")
    public ResponseEntity<SessionsResponse> confirmarSessaoProfissionais(
            Authentication authentication,
            @PathVariable UUID sessaoId) {

        Sessions session = professionalService.confirmarSessao(authentication, sessaoId);
        SessionsResponse response = mapearParaResponse(session);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/profissionais/{sessaoId}/rejeitar")
    public ResponseEntity<SessionsResponse> rejeitarSessaoProfissionais(
            Authentication authentication,
            @PathVariable UUID sessaoId,
            @RequestBody String motivo) {

        Sessions session = professionalService.rejeitarSessao(authentication, sessaoId, motivo);
        SessionsResponse response = mapearParaResponse(session);
        return ResponseEntity.ok(response);
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
    public ResponseEntity<SessionsResponse> reagendarSessaoProfissionais(
            Authentication authentication,
            @PathVariable UUID sessaoId,
            @Valid @RequestBody SessionsRequest request) {

        Sessions session = professionalService.reagendarSessao(authentication, sessaoId, request.dateTime(), request);
        SessionsResponse response = mapearParaResponse(session);
        return ResponseEntity.ok(response);
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
    public ResponseEntity<SessionsResponse> marcarSessaoCuidadorCuidadores(
            Authentication authentication,
            @PathVariable UUID patientId,
            @PathVariable UUID professionalId,
            @Valid @RequestBody SessionsRequest request) {

        Sessions session = caregiverService.marcarSessaoParaPaciente(authentication, patientId, professionalId, request);
        SessionsResponse response = mapearParaResponse(session);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/cuidadores/{sessaoId}/desmarcar")
    public ResponseEntity<Void> desmarcarSessaoCuidadores(
            Authentication authentication,
            @PathVariable UUID sessaoId) {

        caregiverService.desmarcarSessao(authentication, sessaoId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/cuidadores/{sessaoId}/confirmar")
    public ResponseEntity<SessionsResponse> confirmarSessaoCuidadores(
            Authentication authentication,
            @PathVariable UUID sessaoId) {

        Sessions session = caregiverService.confirmarSessao(authentication, sessaoId);
        SessionsResponse response = mapearParaResponse(session);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cuidadores/{sessaoId}/rejeitar")
    public ResponseEntity<SessionsResponse> rejeitarSessaoCuidadores(
            Authentication authentication,
            @PathVariable UUID sessaoId,
            @RequestBody String motivo) {

        Sessions session = caregiverService.rejeitarSessao(authentication, sessaoId, motivo);
        SessionsResponse response = mapearParaResponse(session);
        return ResponseEntity.ok(response);
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
    public ResponseEntity<SessionsResponse> reagendarSessaoCuidadores(
            Authentication authentication,
            @PathVariable UUID sessaoId,
            @Valid @RequestBody SessionsRequest request) {

        Sessions session = caregiverService.reagendarSessao(authentication, sessaoId, request.dateTime(), request);
        SessionsResponse response = mapearParaResponse(session);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cuidadores/todas-sessoes/{patientId}")
    public ResponseEntity<Page<SessionsResponse>> listarTodasSessoesCuidadores(
            Authentication authentication,
            @PathVariable UUID patientId,
            @RequestParam(required = false) SessionsStatus status,
            @PageableDefault(size = 10, sort = "dateTime", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<SessionsResponse> page = caregiverService.listarTodasSessoesDoPaciente(authentication, patientId, status, pageable);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/cuidadores/sessoes-anteriores/{patientId}")
    public ResponseEntity<Page<SessionsResponse>> listarSessoesAnterioresCuidadores(
            Authentication authentication,
            @PathVariable UUID patientId,
            @PageableDefault(size = 10, sort = "dateTime", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<SessionsResponse> page = caregiverService.listarSessoesAnterioresDoPaciente(authentication, patientId, pageable);
        return ResponseEntity.ok(page);
    }


    // ========== Paciente ==========
    @PostMapping("/pacientes/marcar-sessao/{professionalId}")
    public ResponseEntity<SessionsResponse> marcarSessaoPaciente(
            Authentication authentication,
            @PathVariable UUID professionalId,
            @Valid @RequestBody SessionsRequest request) {

        Sessions session = patientService.marcarSessaoParaPaciente(authentication, professionalId, request);
        SessionsResponse response = mapearParaResponse(session);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/pacientes/{sessaoId}/desmarcar")
    public ResponseEntity<Void> desmarcarSessaoPaciente(
            Authentication authentication,
            @PathVariable UUID sessaoId) {

        patientService.desmarcarSessao(authentication, sessaoId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/pacientes/{sessaoId}/confirmar")
    public ResponseEntity<SessionsResponse> confirmarSessaoPaciente(
            Authentication authentication,
            @PathVariable UUID sessaoId) {

        Sessions session = patientService.confirmarSessao(authentication, sessaoId);
        SessionsResponse response = mapearParaResponse(session);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/pacientes/{sessaoId}/rejeitar")
    public ResponseEntity<SessionsResponse> rejeitarSessaoPaciente(
            Authentication authentication,
            @PathVariable UUID sessaoId,
            @RequestBody String motivo) {

        Sessions session = patientService.rejeitarSessao(authentication, sessaoId, motivo);
        SessionsResponse response = mapearParaResponse(session);
        return ResponseEntity.ok(response);
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
    public ResponseEntity<SessionsResponse> reagendarSessaoPaciente(
            Authentication authentication,
            @PathVariable UUID sessaoId,
            @Valid @RequestBody SessionsRequest request) {

        Sessions session = patientService.reagendarSessao(authentication, sessaoId, request.dateTime(), request);
        SessionsResponse response = mapearParaResponse(session);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pacientes/todas-sessoes")
    public ResponseEntity<Page<SessionsResponse>> listarTodasSessoesPaciente(
            Authentication authentication,
            @RequestParam(required = false) Status status,
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


    // Auxiliar
    private SessionsResponse mapearParaResponse(Sessions s) {
        return new SessionsResponse(
                s.getId(),
                s.getDateTime(),
                s.getRemote(),
                s.getPlace(),
                s.getPatient().getNameComplete(),
                s.getPatient().getIdUser(),
                s.getPatient().getNameComplete(),
                s.getProfessional().getNameComplete(),
                s.getProfessional().getIdUser(),
                s.getStatus(),
                s.getReasonChange(),
                s.getCaregiver() != null ? s.getCaregiver().getIdUser() : null
        );
    }
}

