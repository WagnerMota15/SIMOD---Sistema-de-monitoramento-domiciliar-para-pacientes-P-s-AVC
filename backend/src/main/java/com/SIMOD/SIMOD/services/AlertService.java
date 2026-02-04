package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.domain.model.mensagens.Alert;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.dto.Messages.AlertRequest;
import com.SIMOD.SIMOD.repositories.AlertRepository;
import com.SIMOD.SIMOD.repositories.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public Alert criarAlerta(UUID patientId, AlertRequest request) {
        UUID usuarioLogadoId = getUsuarioLogadoId();

        if (!patientId.equals(usuarioLogadoId)) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Você não pode criar alertas para outro paciente"
            );
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        Alert alert = Alert.builder()
                .patientId(patientId)
                .type(request.tipo())
                .description(request.descricao())
                .severity(request.nivel())
                .resolved(false)
                .createdAt(LocalDateTime.now())
                .build();

        return alertRepository.save(alert);
    }

    @Transactional(readOnly = true)
    public List<Alert> listarAlertasPaciente(UUID patientId) {
        UUID usuarioLogadoId = getUsuarioLogadoId();

        if (!patientId.equals(usuarioLogadoId)) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Você não pode visualizar alertas de outro paciente"
            );
        }

        return alertRepository.findByPatientId(patientId);
    }

    @Transactional
    public void resolverAlerta(UUID alertId, UUID id) {
        UUID usuarioLogadoId = getUsuarioLogadoId();

        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new EntityNotFoundException("Alerta não encontrado"));

        if (!alert.getPatientId().equals(usuarioLogadoId)) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Você não pode resolver alertas de outro paciente"
            );
        }

        alert.setResolved(true);
        alertRepository.save(alert);
    }

    private UUID getUsuarioLogadoId() {
        var auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication();

        var userDetails = (com.SIMOD.SIMOD.config.UserDetailsImpl) auth.getPrincipal();
        return userDetails.getUser().getIdUser();
    }
}

