package com.SIMOD.SIMOD.controller.plansTreatment;

import com.SIMOD.SIMOD.domain.model.atividades.Activities;
import com.SIMOD.SIMOD.dto.plansTreatment.ActivitiesRequest;
import com.SIMOD.SIMOD.dto.plansTreatment.ActivitiesResponse;
import com.SIMOD.SIMOD.services.ActivitiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/atividades")
@RequiredArgsConstructor
public class ActivitiesController {
    private final ActivitiesService activitiesService;

    // ====== FISIOTERAPEUTAS E FONOAUDIOLOGOS ======

    @PostMapping("/fisio-fono/prescrever/{patientId}")
    public ResponseEntity<ActivitiesResponse> prescreverAtividade(
            Authentication authentication,
            @PathVariable UUID patientId,
            @RequestBody ActivitiesRequest request
    ) {
        Activities activities = activitiesService.prescreverAtividade(authentication, patientId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapToResponse(activities));
    }

    @PutMapping("/fisio-fono/editar/{activitieId}")
    public ResponseEntity<ActivitiesResponse> editarAtividade(
            Authentication authentication,
            @PathVariable UUID activitieId,
            @RequestBody ActivitiesRequest request
    ) {
        ActivitiesResponse response =
                activitiesService.editarAtividade(authentication, activitieId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/fisio-fono/inativar/{activitieId}")
    public ResponseEntity<Void> inativarAtividade(
            Authentication authentication,
            @PathVariable UUID activitieId
    ) {
        activitiesService.inativarAtividade(authentication, activitieId);
        return ResponseEntity.noContent().build();
    }

    // ====== CONSULTAS ======

    @GetMapping("/usuarios/listar-atividades")
    public Page<ActivitiesResponse> listarAtividades(
            Authentication authentication,
            Pageable pageable
    ) {
        return activitiesService.listarAtividades(authentication, pageable);
    }

    @GetMapping("/usuarios/listar-atividades-ativas")
    public Page<ActivitiesResponse> listarAtividadesAtivas(
            Authentication authentication,
            Pageable pageable
    ) {
        return activitiesService.listarAtividadesAtivas(authentication, pageable);
    }

    @GetMapping("/usuarios/listar-atividades-inativas")
    public Page<ActivitiesResponse> listarAtividadesInativas(
            Authentication authentication,
            Pageable pageable
    ) {
        return activitiesService.listarAtividadesInativas(authentication, pageable);
    }

    @GetMapping("/usuarios/listar-atividades-paciente/{patientId}")
    public Page<ActivitiesResponse> listarPorPaciente(
            Authentication authentication,
            @PathVariable UUID patientId,
            Pageable pageable
    ) {
        return activitiesService.listarAtividadePorPaciente(authentication, patientId, pageable);
    }

    @GetMapping("/usuarios/listar-atividade/{activitiesId}")
    public ActivitiesResponse buscarAtividade(
            Authentication authentication,
            @PathVariable UUID activitiesId
    ) {
        return activitiesService.buscarAtividade(authentication, activitiesId);
    }

    private ActivitiesResponse mapToResponse(Activities activities) {
        return new ActivitiesResponse(
                activities.getId(),
                activities.getName(),
                activities.getDescription(),
                activities.getType(),
                activities.getFreqRecommended(),
                activities.getVideoUrl(),
                activities.getStatus(),
                activities.getPatient().getIdUser(),
                activities.getProfessional().getIdUser()
        );
    }
}
