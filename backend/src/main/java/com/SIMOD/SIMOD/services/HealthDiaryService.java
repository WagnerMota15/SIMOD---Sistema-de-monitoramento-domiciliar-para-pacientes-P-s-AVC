package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.config.UserDetailsImpl;
import com.SIMOD.SIMOD.domain.enums.SessionsStatus;
import com.SIMOD.SIMOD.domain.enums.Status;
import com.SIMOD.SIMOD.domain.enums.TipoNotificacao;
import com.SIMOD.SIMOD.domain.enums.VinculoStatus;
import com.SIMOD.SIMOD.domain.model.associacoes.CaregiverPatient;
import com.SIMOD.SIMOD.domain.model.associacoes.PatientProfessional;
import com.SIMOD.SIMOD.domain.model.atividades.Activities;
import com.SIMOD.SIMOD.domain.model.cuidador.Caregiver;
import com.SIMOD.SIMOD.domain.model.diario.*;
import com.SIMOD.SIMOD.domain.model.dieta.Diet;
import com.SIMOD.SIMOD.domain.model.medicamentos.Medicines;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import com.SIMOD.SIMOD.domain.model.sessoes.Sessions;
import com.SIMOD.SIMOD.domain.model.usuario.User;
import com.SIMOD.SIMOD.dto.Messages.NotificationsRequest;
import com.SIMOD.SIMOD.dto.diario.*;
import com.SIMOD.SIMOD.mapper.diario.HealthDiaryMapper;
import com.SIMOD.SIMOD.repositories.*;
import com.SIMOD.SIMOD.services.firebase.NotificationFacadeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.singletonList;

@Service
@RequiredArgsConstructor
public class HealthDiaryService {

    private final HealthDiaryRepository healthDiaryRepository;
    private final PatientRepository patientRepository;
    private final CaregiverRepository caregiverRepository;
    private final ProfessionalRepository professionalRepository;
    private final CaregiverPatientRepository caregiverPatientRepository;
    private final PatientProfessionalRepository patientProfessionalRepository;
    private final NotificationFacadeService notificationFacadeService;
    private final MedicinesRepository medicinesRepository;
    private final ActivitiesRepository  activitiesRepository;
    private final DietRepository  dietRepository;
    private final SessionsRepository  sessionsRepository;
    private final HealthDiaryMapper mapper;


    @Transactional
    public HealthDiaryResponse registrarDiario(Authentication authentication, UUID patientId, HealthDiaryRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));
        User user = getUsuarioLogado(authentication);

        Caregiver caregiver = null;
        boolean isPatient = user.getIdUser().equals(patient.getIdUser());
        boolean isCaregiver = false;

        // verifica se é cuidador com vínculo
        if (!isPatient) {
            caregiver = getCuidadorLogado(authentication);
            if (caregiverPatientRepository.existsByCaregiverAndPatientAndStatus(caregiver, patient, VinculoStatus.ACEITO)) {
                isCaregiver = true;
            }
        }

        if (!isPatient && !isCaregiver) {
            throw new IllegalStateException("Sem permissão para registrar ou editar este diário");
        }

        // só permite diário do dia atual ou anterior
        LocalDate today = LocalDate.now();
        if (request.diaryDate().isBefore(today.minusDays(1)) || request.diaryDate().isAfter(today)) {
            throw new IllegalStateException("Só é permitido registrar/editar diário do dia atual ou anterior");
        }

        // busca diário existente do dia
        HealthDiary diary = healthDiaryRepository
                .findByPatientAndDiaryDate(patient, request.diaryDate())
                .orElse(HealthDiary.builder()
                        .patient(patient)
                        .caregiver(isCaregiver ? caregiver : null)
                        .diaryDate(request.diaryDate())
                        .build()
                );

        // atualiza campos
        diary.setSystolicBp(request.systolicBp());
        diary.setDiastolicBp(request.diastolicBp());
        diary.setHeartRate(request.heartRate());
        diary.setWeight(request.weight());
        diary.setSymptoms(request.symptoms());
        diary.setGlucose(request.glucose());
        atualizarMedicamentos(diary, request);
        atualizarDietas(diary, request);
        atualizarAtividades(diary, request);
        atualizarSessoes(diary, request);

        HealthDiary saved = healthDiaryRepository.save(diary);

        List<CaregiverPatient> caregivers = caregiverPatientRepository.findByPatientAndStatus(patient, VinculoStatus.ACEITO);
        for (CaregiverPatient cp : caregivers) {
            Caregiver c = cp.getCaregiver();
            if (c != null && c.getIdUser() != null) {
                NotificationsRequest notif = new NotificationsRequest(
                        "Diário atualizado",
                        "O paciente " + patient.getNameComplete() + " registrou/atualizou o diário de saúde.",
                        TipoNotificacao.INFO
                );
                notificationFacadeService.notify(c.getIdUser(), notif);
            }
        }

        List<PatientProfessional> professionals = patientProfessionalRepository.findByPatientAndStatus(patient, VinculoStatus.ACEITO);
        for (PatientProfessional pp : professionals) {
            Professional prof = pp.getProfessional();
            if (prof != null && prof.getIdUser() != null) {
                NotificationsRequest notif = new NotificationsRequest(
                        "Diário atualizado",
                        "O paciente " + patient.getNameComplete() + " registrou/atualizou o diário de saúde.",
                        TipoNotificacao.INFO
                );
                notificationFacadeService.notify(prof.getIdUser(), notif);
            }
        }

        return mapper.toResponse(diary);
    }


    @Transactional(readOnly = true)
    public Page<HealthDiaryResponse> listarDiarios(Authentication authentication, UUID patientId, Pageable pageable) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));
        User user = getUsuarioLogado(authentication);

        // verifica acesso
        if (!user.getIdUser().equals(patient.getIdUser()) &&
                !isCaregiverLinked(authentication, patient) &&
                !isProfessionalLinked(authentication, patient)) {
            throw new IllegalStateException("Sem permissão para visualizar este diário");
        }

        return healthDiaryRepository.findByPatientOrderByDiaryDateDesc(patient, pageable)
                .map(this.mapper::toResponse);
    }


    @Transactional(readOnly = true)
    public HealthDiaryResponse buscarDiario(Authentication authentication, UUID patientId, LocalDate date) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));
        User user = getUsuarioLogado(authentication);

        if (!user.getIdUser().equals(patient.getIdUser()) &&
                !isCaregiverLinked(authentication, patient) &&
                !isProfessionalLinked(authentication, patient)) {
            throw new IllegalStateException("Sem permissão para visualizar este diário");
        }

        Caregiver caregiver = isCaregiverLinked(authentication, patient)
                ? getCuidadorLogado(authentication) : null;
        HealthDiary diary = getOrCreateDiary(patient, date, caregiver);

        return mapper.toResponse(diary);
    }



    // Auxiliares
    private User getUsuarioLogado(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getUser();
    }

    private Caregiver getCuidadorLogado(Authentication authentication) {
        User user = getUsuarioLogado(authentication);
        return caregiverRepository.findByIdUser(user.getIdUser())
                .orElseThrow(() -> new EntityNotFoundException("Cuidador não encontrado"));
    }

    private boolean isCaregiverLinked(Authentication authentication, Patient patient) {
        User user = getUsuarioLogado(authentication);
        return caregiverRepository.findByIdUser(user.getIdUser())
                .map(c -> caregiverPatientRepository
                        .existsByCaregiverAndPatientAndStatus(c, patient, VinculoStatus.ACEITO))
                .orElse(false);
    }


    private boolean isProfessionalLinked(Authentication authentication, Patient patient) {
        User user = getUsuarioLogado(authentication);
        Professional prof = (Professional) professionalRepository.findByIdUser(user.getIdUser()).orElse(null);
        if (prof == null) return false;
        return patientProfessionalRepository.existsByPatientAndProfessionalAndStatus(patient, prof, VinculoStatus.ACEITO);
    }

    private HealthDiary getOrCreateDiary(Patient patient, LocalDate date, Caregiver caregiver) {
        return healthDiaryRepository
                .findByPatientAndDiaryDate(patient, date)
                .orElseGet(() -> {
                    HealthDiary diary = HealthDiary.builder()
                            .patient(patient)
                            .caregiver(caregiver)
                            .diaryDate(date)
                            .build();

                    popularPlanoInicial(diary);

                    return healthDiaryRepository.save(diary);
                });
    }

    private void atualizarMedicamentos(HealthDiary diary, HealthDiaryRequest request) {
        diary.getMedicines().clear();

        if (request.medicines() == null) return;

        for (DiaryMedicineRequest req : request.medicines()) {

            Medicines medicine = medicinesRepository.findById(req.medicineId())
                    .orElseThrow(() -> new EntityNotFoundException("Medicamento não encontrado"));

            if (!medicine.getPatient().equals(diary.getPatient())) {
                throw new IllegalStateException("Medicamento não pertence ao paciente");
            }

            HealthDiaryMedicine hdm = HealthDiaryMedicine.builder()
                    .diary(diary)
                    .medicine(medicine)
                    .taken(req.taken())
                    .doseTaken(req.doseTaken())
                    .unityTaken(req.unityTaken())
                    .timeTaken(req.timeTaken() != null
                            ? LocalTime.parse(req.timeTaken())
                            : null)
                    .note(req.note())
                    .build();

            diary.getMedicines().add(hdm);
        }
    }

    private void atualizarDietas(HealthDiary diary, HealthDiaryRequest request) {
        diary.getDiets().clear();

        if (request.diets() == null) return;

        for (DiaryDietRequest req : request.diets()) {

            Diet diet = dietRepository.findById(req.dietId())
                    .orElseThrow(() -> new EntityNotFoundException("Dieta não encontrada"));

            if (!diet.getPatient().equals(diary.getPatient())) {
                throw new IllegalStateException("Dieta não pertence ao paciente");
            }

            HealthDiaryDiet hdd = HealthDiaryDiet.builder()
                    .diary(diary)
                    .diet(diet)
                    .followed(req.followed())
                    .note(req.note())
                    .build();

            diary.getDiets().add(hdd);
        }
    }

    private void atualizarAtividades(HealthDiary diary, HealthDiaryRequest request) {
        diary.getActivities().clear();

        if (request.activities() == null) return;

        for (DiaryActivityRequest req : request.activities()) {

            Activities activity = activitiesRepository.findById(req.activityId())
                    .orElseThrow(() -> new EntityNotFoundException("Atividade não encontrada"));

            if (!activity.getPatient().equals(diary.getPatient())) {
                throw new IllegalStateException("Atividade não pertence ao paciente");
            }

            HealthDiaryActivity hda = HealthDiaryActivity.builder()
                    .diary(diary)
                    .activity(activity)
                    .completed(req.completed())
                    .note(req.note())
                    .build();

            diary.getActivities().add(hda);
        }
    }

    private void atualizarSessoes(HealthDiary diary, HealthDiaryRequest request) {
        diary.getSessions().clear();

        if (request.sessions() == null) return;

        for (DiarySessionRequest req : request.sessions()) {

            Sessions session = sessionsRepository.findById(req.sessionId())
                    .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));

            if (!session.getPatient().equals(diary.getPatient())) {
                throw new IllegalStateException("Sessao não pertence ao paciente");
            }

            HealthDiarySession hds = HealthDiarySession.builder()
                    .diary(diary)
                    .session(session)
                    .attended(req.attended())
                    .note(req.note())
                    .build();

            diary.getSessions().add(hds);
        }
    }

    // Métodos para popular o diário
    private void popularPlanoInicial(HealthDiary diary) {
        UUID patientId = diary.getPatient().getPatientId();

        popularMedicamentos(diary, patientId);
        popularDietas(diary, patientId);
        popularAtividades(diary, patientId);
        popularSessoes(diary, patientId);
    }

    private void popularMedicamentos(HealthDiary diary, UUID patientId) {
        List<Medicines> meds = medicinesRepository
                .findByPatientIdUserAndStatus(patientId, Status.ATIVA);

        for (Medicines med : meds) {
            HealthDiaryMedicine hdm = HealthDiaryMedicine.builder()
                    .diary(diary)
                    .medicine(med)
                    .taken(false)
                    .build();

            diary.getMedicines().add(hdm);
        }
    }

    private void popularDietas(HealthDiary diary, UUID patientId) {
        List<Diet> diets = dietRepository
                .findByPatientIdUserAndStatus(patientId, Status.ATIVA);

        for (Diet diet : diets) {
            HealthDiaryDiet hdd = HealthDiaryDiet.builder()
                    .diary(diary)
                    .diet(diet)
                    .followed(false)
                    .build();

            diary.getDiets().add(hdd);
        }
    }

    private void popularAtividades(HealthDiary diary, UUID patientId) {
        List<Activities> acts = activitiesRepository
                .findByPatientIdUserAndStatus(patientId, Status.ATIVA);

        for (Activities act : acts) {
            HealthDiaryActivity hda = HealthDiaryActivity.builder()
                    .diary(diary)
                    .activity(act)
                    .completed(false)
                    .build();

            diary.getActivities().add(hda);
        }
    }

    private void popularSessoes(HealthDiary diary, UUID patientId) {
        List<Sessions> sessions = sessionsRepository
                .findByPatientIdUserAndStatusIn(patientId, List.of(SessionsStatus.AGENDADA,
                        SessionsStatus.CONFIRMADA,
                        SessionsStatus.REAGENDADA
                ));

        for (Sessions session : sessions) {
            HealthDiarySession hds = HealthDiarySession.builder()
                    .diary(diary)
                    .session(session)
                    .attended(false)
                    .build();

            diary.getSessions().add(hds);
        }
    }
}
