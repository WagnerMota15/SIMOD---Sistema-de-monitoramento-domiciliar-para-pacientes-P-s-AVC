package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.domain.model.mensagens.Reminders;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.dto.Messages.ReminderRequest;
import com.SIMOD.SIMOD.dto.Messages.ReminderResponse;
import com.SIMOD.SIMOD.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RemindersService {
    private final RemindersRepository reminderRepository;
    private final PatientRepository patientRepository;
    private final MedicinesRepository medicinesRepository;
    private final DietRepository dietRepository;
    private final ActivitiesRepository activitiesRepository;
    private final SessionsRepository sessionsRepository;


    /* O lembrete não é necessariamente da mesma forma que o profissional preescreveu a atividade,
    pois quem os configura é o paciente ou cuidador*/
    @Transactional
    public Reminders criarLembrete(UUID patientId, ReminderRequest request) {
        UUID usuarioLogadoId = getUsuarioLogadoId();

        if (!patientId.equals(usuarioLogadoId)) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Você não pode criar lembretes para outro paciente"
            );
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        if (!request.hasValidReference()) {
            throw new IllegalArgumentException(
                    "É obrigatório informar o item referenciado para o tipo de lembrete: " + request.tipo()
            );
        }

        Reminders lembrete = Reminders.builder()
                .patient(patientRepository.getReferenceById(patientId))
                .type(request.tipo())
                .title(request.titulo())
                .description(request.descricao())
                .scheduledAt(request.dataHora())
                .recurring(request.recorrente())
                .intervalType(request.intervalo())
                .intervalHours(request.intervaloHora())
                .createdBy(request.usuarioCadastrante())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        // Preenche o campo correto
        switch (request.tipo()) {
            case MEDICAMENTO -> lembrete.setMedicine(
                    medicinesRepository.findById(request.medicineId())
                            .orElseThrow(() -> new EntityNotFoundException("Medicamento não encontrado"))
            );
            case DIETA -> lembrete.setDiet(
                    dietRepository.findById(request.dietId())
                            .orElseThrow(() -> new EntityNotFoundException("Dieta não encontrada"))
            );
            case EXERCICIO -> lembrete.setActivity(
                    activitiesRepository.findById(request.activityId())
                            .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada"))
            );
            case SESSOES -> lembrete.setSession(
                    sessionsRepository.findById(request.sessionId())
                            .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"))
            );
        }
        return reminderRepository.save(lembrete);
    }


    @Transactional(readOnly = true)
    public List<ReminderResponse> listarLembretes(UUID patientId) {
        UUID usuarioLogadoId = getUsuarioLogadoId();

        if (!patientId.equals(usuarioLogadoId)) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Você não pode visualizar lembretes de outro paciente"
            );
        }

        return reminderRepository.findByPatientIdUserAndActiveTrue(patientId)
                .stream()
                .map(lembrete -> new ReminderResponse(
                        lembrete.getId(),
                        lembrete.getType(),
                        lembrete.getTitle(),
                        lembrete.getDescription(),
                        lembrete.getScheduledAt(),
                        lembrete.getIntervalHours(),
                        lembrete.isRecurring(),
                        lembrete.getIntervalType(),
                        lembrete.isActive()
                ))
                .toList();
    }


    @Transactional
    public void desativarLembrete(UUID patientId, UUID lembreteId) {
        UUID usuarioLogadoId = getUsuarioLogadoId();

        if (!patientId.equals(usuarioLogadoId)) {
            throw new AccessDeniedException(
                    "Você não pode desativar lembretes de outro paciente"
            );
        }

        Reminders lembrete = reminderRepository.findById(lembreteId)
                .orElseThrow(() -> new EntityNotFoundException("Lembrete não encontrado"));

        if (!lembrete.getPatient().getIdUser().equals(usuarioLogadoId)) {
            throw new AccessDeniedException(
                    "Você não pode desativar lembretes de outro paciente"
            );
        }

        lembrete.setActive(false);
        reminderRepository.save(lembrete);
    }


    @Transactional
    public void reativarLembrete(UUID lembreteId) {
        UUID usuarioLogadoId = getUsuarioLogadoId();

        Reminders lembrete = reminderRepository.findById(lembreteId)
                .orElseThrow(() -> new EntityNotFoundException("Lembrete não encontrado"));

        if (!lembrete.getPatient().getIdUser().equals(usuarioLogadoId)) {
            throw new AccessDeniedException(
                    "Você não pode reativar lembretes de outro paciente"
            );
        }

        if (lembrete.isActive()) {
            return; // já está ativo, evita update desnecessário
        }

        lembrete.setActive(true);
    }

    // Auxiliar
    private UUID getUsuarioLogadoId() {
        var auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication();

        var userDetails = (com.SIMOD.SIMOD.config.UserDetailsImpl) auth.getPrincipal();
        return userDetails.getUser().getIdUser();
    }
}

