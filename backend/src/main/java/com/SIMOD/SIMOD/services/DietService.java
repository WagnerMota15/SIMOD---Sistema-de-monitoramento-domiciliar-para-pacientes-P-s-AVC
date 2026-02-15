package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.config.UserDetailsImpl;
import com.SIMOD.SIMOD.domain.enums.*;
import com.SIMOD.SIMOD.domain.model.associacoes.CaregiverPatient;
import com.SIMOD.SIMOD.domain.model.associacoes.PatientProfessional;
import com.SIMOD.SIMOD.domain.model.cuidador.Caregiver;
import com.SIMOD.SIMOD.domain.model.dieta.Diet;
import com.SIMOD.SIMOD.domain.model.medicamentos.Medicines;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import com.SIMOD.SIMOD.domain.model.usuario.User;
import com.SIMOD.SIMOD.dto.Messages.NotificationsRequest;
import com.SIMOD.SIMOD.dto.plansTreatment.DietRequest;
import com.SIMOD.SIMOD.dto.plansTreatment.DietResponse;
import com.SIMOD.SIMOD.dto.plansTreatment.MedicinesResponse;
import com.SIMOD.SIMOD.repositories.*;
import com.SIMOD.SIMOD.services.firebase.NotificationFacadeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DietService {
    private final ProfessionalRepository professionalRepository;
    private final PatientRepository patientRepository;
    private final CaregiverRepository caregiverRepository;
    private final PatientProfessionalRepository patientProfessionalRepository;
    private final CaregiverPatientRepository caregiverPatientRepository;
    private final DietRepository dietRepository;
    private final NotificationFacadeService notificationFacadeService;


    @Transactional
    public Diet prescreverDieta(Authentication authentication, UUID patientId, DietRequest request){
        Professional professional = getProfessionalLogado(authentication);

        if (professional.getRole() != Role.NUTRICIONISTA) {
            throw new IllegalStateException("Apenas nutricionistas podem prescrever dietas");
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        if (!patientProfessionalRepository.existsByPatientAndProfessionalAndStatus(
                patient, professional, VinculoStatus.ACEITO)) {
            throw new IllegalStateException("Não há vínculo ativo com este paciente");
        }

        Diet diet = Diet.builder()
                .freqMeal(request.freq_meal())
                .schedules(request.schedules())
                .description(request.description())
                .status(Status.ATIVA)
                .patient(patient)
                .professional(professional)
                .build();

        Diet savedDiet = dietRepository.save(diet);

        NotificationsRequest notifPaciente = new NotificationsRequest(
                "Nova dieta prescrita",
                "O nutricionista " + professional.getNameComplete() +
                        " prescreveu uma nova dieta para você.",
                TipoNotificacao.INFO
        );
        notificationFacadeService.notify(patient.getIdUser(), notifPaciente);

        notificarTodosCuidadores(patient,
                "Nova dieta prescrita para o paciente",
                "O nutricionista " + professional.getNameComplete() +
                        " prescreveu uma dieta para o paciente " + patient.getNameComplete(),
                TipoNotificacao.INFO
        );

        return savedDiet;
    }


    @Transactional
    public DietResponse editarDieta(Authentication authentication, UUID dietId, DietRequest request) {
        Professional professional = getProfessionalLogado(authentication);

        Diet diet = dietRepository.findById(dietId)
                .orElseThrow(() -> new EntityNotFoundException("Dieta não encontrada"));

        if (!diet.getProfessional().getIdUser().equals(professional.getIdUser())) {
            throw new IllegalStateException("Você não pode editar esta dieta");
        }

        if (professional.getRole() != Role.NUTRICIONISTA) {
            throw new IllegalStateException("Apenas nutricionistas podem prescrever dietas");
        }

        if (diet.getStatus() != Status.ATIVA) {
            throw new IllegalStateException("Apenas dietas ativas podem ser editadas");
        }

        diet.setFreqMeal(request.freq_meal());
        diet.setSchedules(request.schedules());
        diet.setDescription(request.description());

        Diet updated = dietRepository.save(diet);

        NotificationsRequest notifPaciente = new NotificationsRequest(
                "Dieta editada",
                "O nutricionista " + professional.getNameComplete() +
                        " editou uma dieta.",
                TipoNotificacao.INFO
        );
        notificationFacadeService.notify(diet.getPatient().getIdUser(), notifPaciente);

        notificarTodosCuidadores(diet.getPatient(),
                "Dieta editada para o paciente",
                "O nutricionista " + professional.getNameComplete() +
                        " editou uma dieta do paciente " + diet.getPatient().getNameComplete(),
                TipoNotificacao.INFO
        );

        return toResponse(updated);
    }


    @Transactional
    public void inativarDieta(Authentication authentication, UUID dietId) {
        Professional professional = getProfessionalLogado(authentication);

        Diet diet = dietRepository.findById(dietId)
                .orElseThrow(() -> new EntityNotFoundException("Dieta não encontrada"));

        if (!diet.getProfessional().getIdUser().equals(professional.getIdUser())) {
            throw new IllegalStateException("Você não pode inativar esta dieta");
        }

        if (professional.getRole() != Role.NUTRICIONISTA) {
            throw new IllegalStateException("Apenas nutricionistas podem prescrever dietas");
        }

        if (diet.getStatus() == Status.INATIVA) {
            throw new IllegalStateException("Esta dieta já está inativa");
        }

        diet.setStatus(Status.INATIVA);
        dietRepository.save(diet);

        NotificationsRequest notifPaciente = new NotificationsRequest(
                "Dieta inativada",
                "O nutricionista " + professional.getNameComplete() +
                        " inativada uma dieta.",
                TipoNotificacao.INFO
        );
        notificationFacadeService.notify(diet.getPatient().getIdUser(), notifPaciente);

        notificarTodosCuidadores(diet.getPatient(),
                "Dieta inativada para o paciente",
                "O nutricionista " + professional.getNameComplete() +
                        " inativada uma dieta do paciente " + diet.getPatient().getNameComplete(),
                TipoNotificacao.INFO
        );
    }


    @Transactional(readOnly = true)
    public Page<DietResponse> listarDietas(Authentication authentication, Pageable pageable) {
        User user = getUsuarioLogado(authentication);

        return switch (user.getRole()) {
            case PACIENTE -> dietRepository
                    .findByPatientIdUserOrderByCreatedAtDesc(user.getIdUser(), pageable)
                    .map(this::toResponse);

            case NUTRICIONISTA -> {
                Professional professional = getProfessionalLogado(authentication);

                List<PatientProfessional> vinculos = patientProfessionalRepository.findByProfessionalAndStatus(
                        professional, VinculoStatus.ACEITO);

                List<Patient> pacientes = vinculos.stream()
                        .map(PatientProfessional::getPatient)
                        .toList();

                yield dietRepository
                        .findByPatientInOrderByCreatedAtDesc(pacientes, pageable)
                        .map(this::toResponse);
            }

            case CUIDADOR -> {
                Caregiver caregiver = getCuidadorLogado(authentication);

                List<CaregiverPatient> vinculos = caregiverPatientRepository.findByCaregiverAndStatus(
                        caregiver, VinculoStatus.ACEITO);

                List<Patient> pacientes = vinculos.stream()
                        .map(CaregiverPatient::getPatient)
                        .toList();

                yield dietRepository
                        .findByPatientInOrderByCreatedAtDesc(pacientes, pageable)
                        .map(this::toResponse);
            }
            default -> throw new IllegalStateException("Perfil não autorizado");
        };
    }


    @Transactional(readOnly = true)
    public Page<DietResponse> listarDietasAtivas(Authentication authentication, Pageable pageable) {
        User user = getUsuarioLogado(authentication);

        return switch (user.getRole()) {
            case PACIENTE -> dietRepository
                    .findByPatientIdUserAndStatusOrderByCreatedAtDesc(
                            user.getIdUser(),
                            Status.ATIVA,
                            pageable
                    )
                    .map(this::toResponse);

            case NUTRICIONISTA -> {
                Professional professional = getProfessionalLogado(authentication);

                List<Patient> pacientes = patientProfessionalRepository
                        .findByProfessionalAndStatus(professional, VinculoStatus.ACEITO)
                        .stream()
                        .map(PatientProfessional::getPatient)
                        .toList();

                yield dietRepository.findByPatientInAndStatusOrderByCreatedAtDesc(
                        pacientes, Status.ATIVA, pageable).map(this::toResponse);
            }

            case CUIDADOR -> {
                Caregiver caregiver = getCuidadorLogado(authentication);

                List<Patient> pacientes = caregiverPatientRepository
                        .findByCaregiverAndStatus(caregiver, VinculoStatus.ACEITO)
                        .stream()
                        .map(CaregiverPatient::getPatient)
                        .toList();

                yield dietRepository.findByPatientInAndStatusOrderByCreatedAtDesc(
                        pacientes, Status.ATIVA, pageable).map(this::toResponse);
            }

            default -> throw new IllegalStateException("Perfil não autorizado");
        };
    }


    @Transactional(readOnly = true)
    public Page<DietResponse> listarDietasInativas(Authentication authentication, Pageable pageable) {
        User user = getUsuarioLogado(authentication);

        return switch (user.getRole()) {
            case PACIENTE -> dietRepository
                    .findByPatientIdUserAndStatusOrderByCreatedAtDesc(
                            user.getIdUser(),
                            Status.INATIVA,
                            pageable
                    )
                    .map(this::toResponse);

            case NUTRICIONISTA -> {
                Professional professional = getProfessionalLogado(authentication);

                List<Patient> pacientes = patientProfessionalRepository
                        .findByProfessionalAndStatus(professional, VinculoStatus.ACEITO)
                        .stream()
                        .map(PatientProfessional::getPatient)
                        .toList();

                yield dietRepository.findByPatientInAndStatusOrderByCreatedAtDesc(
                        pacientes, Status.INATIVA, pageable).map(this::toResponse);
            }

            case CUIDADOR -> {
                Caregiver caregiver = getCuidadorLogado(authentication);

                List<Patient> pacientes = caregiverPatientRepository
                        .findByCaregiverAndStatus(caregiver, VinculoStatus.ACEITO)
                        .stream()
                        .map(CaregiverPatient::getPatient)
                        .toList();

                yield dietRepository.findByPatientInAndStatusOrderByCreatedAtDesc(
                        pacientes, Status.INATIVA, pageable).map(this::toResponse);
            }

            default -> throw new IllegalStateException("Perfil não autorizado");
        };
    }


    @Transactional(readOnly = true)
    public Page<DietResponse> listarDietasPorPaciente(Authentication authentication, UUID patientId, Pageable pageable) {
        User user = getUsuarioLogado(authentication);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        return switch (user.getRole()) {
            case PACIENTE -> {
                if (!patient.getIdUser().equals(user.getIdUser())) {
                    throw new IllegalStateException("Acesso negado");
                }

                yield dietRepository.findByPatientOrderByCreatedAtDesc(patient, pageable).map(this::toResponse);
            }

            case NUTRICIONISTA -> {
                Professional professional = getProfessionalLogado(authentication);

                if (!patientProfessionalRepository.existsByPatientAndProfessionalAndStatus(
                        patient, professional, VinculoStatus.ACEITO)) {
                    throw new IllegalStateException("Sem vínculo com este paciente");
                }

                yield dietRepository
                        .findByPatientOrderByCreatedAtDesc(patient, pageable).map(this::toResponse);
            }

            case CUIDADOR -> {
                Caregiver caregiver = getCuidadorLogado(authentication);

                if (!caregiverPatientRepository.existsByCaregiverAndPatientAndStatus(
                        caregiver, patient, VinculoStatus.ACEITO)) {
                    throw new IllegalStateException("Sem vínculo com este paciente");
                }

                yield dietRepository.findByPatientOrderByCreatedAtDesc(patient, pageable).map(this::toResponse);
            }

            default -> throw new IllegalStateException("Perfil não autorizado");
        };
    }


    @Transactional(readOnly = true)
    public DietResponse buscarDieta(Authentication authentication, UUID medicineId) {
        User user = getUsuarioLogado(authentication);

        Diet diet = dietRepository.findById(medicineId)
                .orElseThrow(() -> new EntityNotFoundException("Dieta não encontrada"));

        Patient patient = diet.getPatient();

        switch (user.getRole()) {
            case PACIENTE -> {
                if (!patient.getIdUser().equals(user.getIdUser())) {
                    throw new IllegalStateException("Acesso negado a dieta");
                }
            }

            case NUTRICIONISTA -> {
                Professional professional = getProfessionalLogado(authentication);

                if (!patientProfessionalRepository.existsByPatientAndProfessionalAndStatus(
                        patient,
                        professional,
                        VinculoStatus.ACEITO
                )) {
                    throw new IllegalStateException("Sem vínculo com este paciente");
                }
            }

            case CUIDADOR -> {
                Caregiver caregiver = getCuidadorLogado(authentication);

                if (!caregiverPatientRepository.existsByCaregiverAndPatientAndStatus(
                        caregiver,
                        patient,
                        VinculoStatus.ACEITO
                )) {
                    throw new IllegalStateException("Sem vínculo com este paciente");
                }
            }

            default -> throw new IllegalStateException("Perfil não autorizado");
        }

        return toResponse(diet);
    }


    // Auxiliares
    private User getUsuarioLogado(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getUser();
    }


    private Professional getProfessionalLogado(Authentication authentication) {
        User user = getUsuarioLogado(authentication);
        return (Professional) professionalRepository.findByIdUser(user.getIdUser())
                .orElseThrow(() ->
                        new EntityNotFoundException("Profissional não encontrado")
                );
    }


    private Patient getPacienteLogado(Authentication authentication) {
        User user = getUsuarioLogado(authentication);
        return patientRepository.findByIdUser(user.getIdUser())
                .orElseThrow(() ->
                        new EntityNotFoundException("Paciente não encontrado")
                );
    }


    private Caregiver getCuidadorLogado(Authentication authentication) {
        User user = getUsuarioLogado(authentication);
        return caregiverRepository.findByIdUser(user.getIdUser())
                .orElseThrow(() ->
                        new EntityNotFoundException("Cuidador não encontrado")
                );
    }


    private void notificarTodosCuidadores(Patient patient, String titulo, String mensagem, TipoNotificacao tipo) {
        List<CaregiverPatient> vinculos = caregiverPatientRepository.findByPatientAndStatus(patient, VinculoStatus.ACEITO);
        for (CaregiverPatient vinculo : vinculos) {
            Caregiver caregiver = vinculo.getCaregiver();
            if (caregiver != null && caregiver.getIdUser() != null) {
                NotificationsRequest notif = new NotificationsRequest(titulo, mensagem, tipo);
                notificationFacadeService.notify(caregiver.getIdUser(), notif);
            }
        }
    }


    private DietResponse toResponse(Diet diet) {
        return new DietResponse(
                diet.getIdDiet(),
                diet.getFreqMeal(),
                diet.getSchedules(),
                diet.getDescription(),
                diet.getPatient().getIdUser(),
                diet.getProfessional().getIdUser()
        );
    }
}

