package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.config.UserDetailsImpl;
import com.SIMOD.SIMOD.domain.enums.Role;
import com.SIMOD.SIMOD.domain.enums.Status;
import com.SIMOD.SIMOD.domain.enums.TipoNotificacao;
import com.SIMOD.SIMOD.domain.enums.VinculoStatus;
import com.SIMOD.SIMOD.domain.model.associacoes.CaregiverPatient;
import com.SIMOD.SIMOD.domain.model.associacoes.PatientProfessional;
import com.SIMOD.SIMOD.domain.model.atividades.Activities;
import com.SIMOD.SIMOD.domain.model.cuidador.Caregiver;
import com.SIMOD.SIMOD.domain.model.dieta.Diet;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import com.SIMOD.SIMOD.domain.model.usuario.User;
import com.SIMOD.SIMOD.dto.Messages.NotificationsRequest;
import com.SIMOD.SIMOD.dto.plansTreatment.ActivitiesRequest;
import com.SIMOD.SIMOD.dto.plansTreatment.ActivitiesResponse;
import com.SIMOD.SIMOD.dto.plansTreatment.DietRequest;
import com.SIMOD.SIMOD.dto.plansTreatment.DietResponse;
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
public class ActivitiesService {
    private final ProfessionalRepository professionalRepository;
    private final PatientRepository patientRepository;
    private final CaregiverRepository caregiverRepository;
    private final PatientProfessionalRepository patientProfessionalRepository;
    private final CaregiverPatientRepository caregiverPatientRepository;
    private final ActivitiesRepository activitiesRepository;
    private final NotificationFacadeService notificationFacadeService;


    @Transactional
    public Activities prescreverAtividade(Authentication authentication, UUID patientId, ActivitiesRequest request){
        Professional professional = getProfessionalLogado(authentication);

        if (professional.getRole() != Role.FISIOTERAPEUTA && professional.getRole() != Role.FONOAUDIOLOGO) {
            throw new IllegalStateException("Apenas fisioterapeutas ou fonoaudiologos podem prescrever exercicios");
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        if (!patientProfessionalRepository.existsByPatientAndProfessionalAndStatus(
                patient, professional, VinculoStatus.ACEITO)) {
            throw new IllegalStateException("Não há vínculo ativo com este paciente");
        }

        Activities activities = Activities.builder()
                .name(request.name())
                .description(request.description())
                .type(request.type_exercise())
                .freqRecommended(request.freq_recommended())
                .videoUrl(request.video_url())
                .status(Status.ATIVA)
                .patient(patient)
                .professional(professional)
                .build();

        Activities savedActivities = activitiesRepository.save(activities);

        NotificationsRequest notifPaciente = new NotificationsRequest(
                "Nova ativididade prescrita",
                "O " + professional.getRole() + " " + professional.getNameComplete() +
                        " prescreveu uma nova atividade para você.",
                TipoNotificacao.INFO
        );
        notificationFacadeService.notify(patient.getIdUser(), notifPaciente);

        notificarTodosCuidadores(patient,
                "Nova atividade prescrita para o paciente",
                "O " + professional.getRole() + " " + professional.getNameComplete() +
                        " prescreveu uma atividade para o paciente " + patient.getNameComplete(),
                TipoNotificacao.INFO
        );

        return savedActivities;
    }


    @Transactional
    public ActivitiesResponse editarAtividade(Authentication authentication, UUID activitiesId, ActivitiesRequest request) {
        Professional professional = getProfessionalLogado(authentication);

        Activities activities = activitiesRepository.findById(activitiesId)
                .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada"));

        if (!activities.getProfessional().getIdUser().equals(professional.getIdUser())) {
            throw new IllegalStateException("Você não pode editar esta dieta");
        }

        if (professional.getRole() != Role.FISIOTERAPEUTA || professional.getRole() != Role.FONOAUDIOLOGO){
            throw new IllegalStateException("Apenas fisioterapeutas ou fonoaudiologos podem prescrever dietas");
        }

        if (activities.getStatus() != Status.ATIVA) {
            throw new IllegalStateException("Apenas atividades ativas podem ser editadas");
        }

        activities.setDescription(request.description());
        activities.setFreqRecommended(request.freq_recommended());
        activities.setVideoUrl(request.video_url());

        Activities updated = activitiesRepository.save(activities);

        NotificationsRequest notifPaciente = new NotificationsRequest(
                "Atividade editada",
                "O " + professional.getRole() + " " + professional.getNameComplete() +
                        " editou uma atividade.",
                TipoNotificacao.INFO
        );
        notificationFacadeService.notify(activities.getPatient().getIdUser(), notifPaciente);

        notificarTodosCuidadores(activities.getPatient(),
                "Atividade editada para o paciente",
                "O " + professional.getRole() + " " + professional.getNameComplete() +
                        " editou uma atividade do paciente " + activities.getPatient().getNameComplete(),
                TipoNotificacao.INFO
        );

        return toResponse(updated);
    }


    @Transactional
    public void inativarAtividade(Authentication authentication, UUID activitiesId) {
        Professional professional = getProfessionalLogado(authentication);

        Activities activities = activitiesRepository.findById(activitiesId)
                .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada"));

        if (!activities.getProfessional().getIdUser().equals(professional.getIdUser())) {
            throw new IllegalStateException("Você não pode inativar esta atividade");
        }

        if (professional.getRole() != Role.FONOAUDIOLOGO ||  professional.getRole() != Role.FISIOTERAPEUTA) {
            throw new IllegalStateException("Apenas fisioterapeutas ou fonoaudiologos podem prescrever atividades");
        }

        if (activities.getStatus() == Status.INATIVA) {
            throw new IllegalStateException("Esta atividade já está inativa");
        }

        activities.setStatus(Status.INATIVA);
        activitiesRepository.save(activities);

        NotificationsRequest notifPaciente = new NotificationsRequest(
                "Atividade inativada",
                "O " + professional.getRole() + " " + professional.getNameComplete() +
                        " inativou uma atividade.",
                TipoNotificacao.INFO
        );
        notificationFacadeService.notify(activities.getPatient().getIdUser(), notifPaciente);

        notificarTodosCuidadores(activities.getPatient(),
                "Atividade inativada para o paciente",
                "O " + professional.getRole() + " " + professional.getNameComplete() +
                        " inativou uma atividade do paciente " + activities.getPatient().getNameComplete(),
                TipoNotificacao.INFO
        );
    }


    @Transactional(readOnly = true)
    public Page<ActivitiesResponse> listarAtividades(Authentication authentication, Pageable pageable) {
        User user = getUsuarioLogado(authentication);

        return switch (user.getRole()) {
            case PACIENTE -> activitiesRepository
                    .findByPatientIdUserOrderByCreatedAtDesc(user.getIdUser(), pageable)
                    .map(this::toResponse);

            case FISIOTERAPEUTA, FONOAUDIOLOGO -> {
                Professional professional = getProfessionalLogado(authentication);

                List<PatientProfessional> vinculos = patientProfessionalRepository.findByProfessionalAndStatus(
                        professional, VinculoStatus.ACEITO);

                List<Patient> pacientes = vinculos.stream()
                        .map(PatientProfessional::getPatient)
                        .toList();

                yield activitiesRepository
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

                yield activitiesRepository
                        .findByPatientInOrderByCreatedAtDesc(pacientes, pageable)
                        .map(this::toResponse);
            }
            default -> throw new IllegalStateException("Perfil não autorizado");
        };
    }


    @Transactional(readOnly = true)
    public Page<ActivitiesResponse> listarAtividadesAtivas(Authentication authentication, Pageable pageable) {
        User user = getUsuarioLogado(authentication);

        return switch (user.getRole()) {
            case PACIENTE -> activitiesRepository
                    .findByPatientIdUserAndStatusOrderByCreatedAtDesc(
                            user.getIdUser(),
                            Status.ATIVA,
                            pageable
                    )
                    .map(this::toResponse);

            case FISIOTERAPEUTA, FONOAUDIOLOGO -> {
                Professional professional = getProfessionalLogado(authentication);

                List<Patient> pacientes = patientProfessionalRepository
                        .findByProfessionalAndStatus(professional, VinculoStatus.ACEITO)
                        .stream()
                        .map(PatientProfessional::getPatient)
                        .toList();

                yield activitiesRepository.findByPatientInAndStatusOrderByCreatedAtDesc(
                        pacientes, Status.ATIVA, pageable).map(this::toResponse);
            }

            case CUIDADOR -> {
                Caregiver caregiver = getCuidadorLogado(authentication);

                List<Patient> pacientes = caregiverPatientRepository
                        .findByCaregiverAndStatus(caregiver, VinculoStatus.ACEITO)
                        .stream()
                        .map(CaregiverPatient::getPatient)
                        .toList();

                yield activitiesRepository.findByPatientInAndStatusOrderByCreatedAtDesc(
                        pacientes, Status.ATIVA, pageable).map(this::toResponse);
            }

            default -> throw new IllegalStateException("Perfil não autorizado");
        };
    }


    @Transactional(readOnly = true)
    public Page<ActivitiesResponse> listarAtividadesInativas(Authentication authentication, Pageable pageable) {
        User user = getUsuarioLogado(authentication);

        return switch (user.getRole()) {
            case PACIENTE -> activitiesRepository
                    .findByPatientIdUserAndStatusOrderByCreatedAtDesc(
                            user.getIdUser(),
                            Status.INATIVA,
                            pageable
                    )
                    .map(this::toResponse);

            case FISIOTERAPEUTA, FONOAUDIOLOGO -> {
                Professional professional = getProfessionalLogado(authentication);

                List<Patient> pacientes = patientProfessionalRepository
                        .findByProfessionalAndStatus(professional, VinculoStatus.ACEITO)
                        .stream()
                        .map(PatientProfessional::getPatient)
                        .toList();

                yield activitiesRepository.findByPatientInAndStatusOrderByCreatedAtDesc(
                        pacientes, Status.INATIVA, pageable).map(this::toResponse);
            }

            case CUIDADOR -> {
                Caregiver caregiver = getCuidadorLogado(authentication);

                List<Patient> pacientes = caregiverPatientRepository
                        .findByCaregiverAndStatus(caregiver, VinculoStatus.ACEITO)
                        .stream()
                        .map(CaregiverPatient::getPatient)
                        .toList();

                yield activitiesRepository.findByPatientInAndStatusOrderByCreatedAtDesc(
                        pacientes, Status.INATIVA, pageable).map(this::toResponse);
            }

            default -> throw new IllegalStateException("Perfil não autorizado");
        };
    }


    @Transactional(readOnly = true)
    public Page<ActivitiesResponse> listarAtividadePorPaciente(Authentication authentication, UUID patientId, Pageable pageable) {
        User user = getUsuarioLogado(authentication);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        return switch (user.getRole()) {
            case PACIENTE -> {
                if (!patient.getIdUser().equals(user.getIdUser())) {
                    throw new IllegalStateException("Acesso negado");
                }

                yield activitiesRepository.findByPatientOrderByCreatedAtDesc(patient, pageable).map(this::toResponse);
            }

            case FISIOTERAPEUTA, FONOAUDIOLOGO -> {
                Professional professional = getProfessionalLogado(authentication);

                if (!patientProfessionalRepository.existsByPatientAndProfessionalAndStatus(
                        patient, professional, VinculoStatus.ACEITO)) {
                    throw new IllegalStateException("Sem vínculo com este paciente");
                }

                yield activitiesRepository
                        .findByPatientOrderByCreatedAtDesc(patient, pageable).map(this::toResponse);
            }

            case CUIDADOR -> {
                Caregiver caregiver = getCuidadorLogado(authentication);

                if (!caregiverPatientRepository.existsByCaregiverAndPatientAndStatus(
                        caregiver, patient, VinculoStatus.ACEITO)) {
                    throw new IllegalStateException("Sem vínculo com este paciente");
                }

                yield activitiesRepository.findByPatientOrderByCreatedAtDesc(patient, pageable).map(this::toResponse);
            }

            default -> throw new IllegalStateException("Perfil não autorizado");
        };
    }


    @Transactional(readOnly = true)
    public ActivitiesResponse buscarAtividade(Authentication authentication, UUID activitiesId) {
        User user = getUsuarioLogado(authentication);

        Activities activities = activitiesRepository.findById(activitiesId)
                .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada"));

        Patient patient = activities.getPatient();

        switch (user.getRole()) {
            case PACIENTE -> {
                if (!patient.getIdUser().equals(user.getIdUser())) {
                    throw new IllegalStateException("Acesso negado a atividade");
                }
            }

            case FISIOTERAPEUTA, FONOAUDIOLOGO -> {
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

        return toResponse(activities);
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


    private ActivitiesResponse toResponse(Activities activities) {
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
