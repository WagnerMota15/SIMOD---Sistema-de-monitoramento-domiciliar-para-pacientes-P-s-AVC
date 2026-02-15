package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.config.UserDetailsImpl;
import com.SIMOD.SIMOD.domain.enums.*;
import com.SIMOD.SIMOD.domain.enums.TipoLembrete;
import com.SIMOD.SIMOD.domain.model.associacoes.CaregiverPatient;
import com.SIMOD.SIMOD.domain.model.associacoes.PatientProfessional;
import com.SIMOD.SIMOD.domain.model.atividades.Activities;
import com.SIMOD.SIMOD.domain.model.cuidador.Caregiver;
import com.SIMOD.SIMOD.domain.model.diario.*;
import com.SIMOD.SIMOD.domain.model.dieta.Diet;
import com.SIMOD.SIMOD.domain.model.medicamentos.Medicines;
import com.SIMOD.SIMOD.domain.model.mensagens.Reminders;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import com.SIMOD.SIMOD.domain.model.sessoes.Sessions;
import com.SIMOD.SIMOD.domain.model.usuario.User;
import com.SIMOD.SIMOD.dto.Messages.NotificationsRequest;
import com.SIMOD.SIMOD.dto.diario.*;
import com.SIMOD.SIMOD.mapper.diario.HealthDiaryMapper;
import com.SIMOD.SIMOD.domain.model.diario.HealthDiary;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiConsumer;

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
    private final ActivitiesRepository activitiesRepository;
    private final DietRepository dietRepository;
    private final SessionsRepository sessionsRepository;
    private final HealthDiaryMapper mapper;
    private final RemindersRepository remindersRepository;


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
                        .patientName(patient.getNameComplete())
                        .diaryDate(request.diaryDate())
                        .build()
                );


        diary.setSystolicBp(request.systolicBp());
        diary.setDiastolicBp(request.diastolicBp());
        diary.setHeartRate(request.heartRate());
        diary.setWeight(request.weight());
        diary.setSymptoms(request.symptoms());
        diary.setGlucose(request.glucose());

        popularDiarioComLembretes(diary);

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

        LocalDate today = LocalDate.now();
        LocalDate limiteInferior = today.minusDays(30);
        Page<HealthDiary> diaries = healthDiaryRepository.findByPatientAndDiaryDateGreaterThanEqualOrderByDiaryDateDesc(
                patient,
                limiteInferior,
                pageable
        );

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

        LocalDate today = LocalDate.now();
        LocalDate limiteInferior = today.minusDays(90);
        if (date.isAfter(today)) {
            throw new IllegalArgumentException("Não é permitido visualizar diários de datas futuras.");
        }

        if (date.isBefore(limiteInferior)) {
            throw new IllegalArgumentException("Só é permitido visualizar diários dos últimos 90 dias.");
        }

        Caregiver caregiver = isCaregiverLinked(authentication, patient)
                ? getCuidadorLogado(authentication) : null;
        HealthDiary diary = getOrCreateDiary(patient, date, caregiver);

        return mapper.toResponse(diary);
    }


    @Transactional(readOnly = true)
    public Page<HealthDiaryResponse> listarDiariosVinculadosCaregiver(Authentication authentication, Pageable pageable) {
        Caregiver caregiver = getCuidadorLogado(authentication);

        List<Patient> pacientes = caregiverPatientRepository.findByCaregiverAndStatus(caregiver, VinculoStatus.ACEITO)
                .stream()
                .map(CaregiverPatient::getPatient)
                .toList();

        Page<HealthDiary> diaries = healthDiaryRepository.findByPatientInOrderByDiaryDateDesc(pacientes, pageable);

        return diaries.map(this.mapper::toResponse);
    }


    @Transactional(readOnly = true)
    public Page<HealthDiaryResponse> listarDiariosVinculadosProfessional(Authentication authentication, Pageable pageable) {
        User user = getUsuarioLogado(authentication);
        Professional prof = (Professional) professionalRepository.findByIdUser(user.getIdUser())
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

        List<Patient> pacientes = patientProfessionalRepository.findByProfessionalAndStatus(prof, VinculoStatus.ACEITO)
                .stream()
                .map(PatientProfessional::getPatient)
                .toList();

        Page<HealthDiary> diaries = healthDiaryRepository.findByPatientInOrderByDiaryDateDesc(pacientes, pageable);

        return diaries.map(this.mapper::toResponse);
    }


    // Confirmações de atividades
    @Transactional
    public ReminderCompletedResponse confirmarMedicamento(Authentication authentication, UUID diaryId, UUID healthDiaryMedicineId) {
        HealthDiary diary = healthDiaryRepository.findById(diaryId)
                .orElseThrow(() -> new EntityNotFoundException("Diário não encontrado"));

        Patient patient = diary.getPatient();
        if (patient == null) {
            throw new IllegalStateException("Paciente não associado ao diário");
        }

        User user = getUsuarioLogado(authentication);

        boolean isPatient = user.getIdUser().equals(patient.getIdUser());
        boolean isCaregiver = false;

        if (!isPatient) {
            Caregiver caregiver = getCuidadorLogado(authentication);
            isCaregiver = caregiverPatientRepository.existsByCaregiverAndPatientAndStatus(
                    caregiver,
                    patient,
                    VinculoStatus.ACEITO
            );
        }

        if (!isPatient && !isCaregiver) {
            throw new IllegalStateException("Sem permissão para confirmar entradas neste diário. " +
                    "Apenas o paciente dono ou cuidadores vinculados podem realizar esta ação.");
        }

        HealthDiaryMedicine entry = diary.getMedicines().stream()
                .filter(medicine -> medicine.getId().equals(healthDiaryMedicineId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Entrada de medicamento não encontrada"));

        if (entry.isTaken()) {
            throw new IllegalStateException("Este medicamento já foi marcado como tomado");
        }

        entry.setTaken(true);

        Reminders reminder = entry.getMedicine() != null
                ? remindersRepository.findByMedicineAndScheduledAtAndPatient(
                entry.getMedicine(),
                entry.getTimeTaken().atDate(diary.getDiaryDate()),
                diary.getPatient()
        ).orElse(null)
                : null;

        if (reminder == null) {
            throw new IllegalStateException("Lembrete original não encontrado para esta entrada");
        }

        reminder.setConfirmed(true);
        reminder.setConfirmedAt(LocalDateTime.now());

        remindersRepository.save(reminder);

        entry.setTaken(true);

        healthDiaryRepository.save(diary);

        return new ReminderCompletedResponse(
                diary.getId(),
                diary.getPatient().getNameComplete(),
                true,
                "Medicamento confirmado com sucesso"
        );
    }


    @Transactional
    public ReminderCompletedResponse confirmarDieta(Authentication authentication, UUID diaryId, UUID healthDiaryDietaId) {
        HealthDiary diary = healthDiaryRepository.findById(diaryId)
                .orElseThrow(() -> new EntityNotFoundException("Diário não encontrado"));

        Patient patient = diary.getPatient();
        if (patient == null) {
            throw new IllegalStateException("Paciente não associado ao diário");
        }

        User user = getUsuarioLogado(authentication);

        boolean isPatient = user.getIdUser().equals(patient.getIdUser());
        boolean isCaregiver = false;

        if (!isPatient) {
            Caregiver caregiver = getCuidadorLogado(authentication);
            isCaregiver = caregiverPatientRepository.existsByCaregiverAndPatientAndStatus(
                    caregiver,
                    patient,
                    VinculoStatus.ACEITO
            );
        }

        if (!isPatient && !isCaregiver) {
            throw new IllegalStateException("Sem permissão para confirmar entradas neste diário. " +
                    "Apenas o paciente dono ou cuidadores vinculados podem realizar esta ação.");
        }

        HealthDiaryDiet entry = diary.getDiets().stream()
                .filter(diet -> diet.getId().equals(healthDiaryDietaId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Entrada de dieta não encontrada"));

        if (entry.isFollowed()) {
            throw new IllegalStateException("Esta dieta já foi marcado como seguida");
        }

        entry.setFollowed(true);

        Reminders reminder = entry.getDiet() != null
                ? remindersRepository.findByDietAndScheduledAtAndPatient(
                entry.getDiet(),
                entry.getTimeFollowed().atDate(diary.getDiaryDate()),
                diary.getPatient()
        ).orElse(null)
                : null;

        if (reminder == null) {
            throw new IllegalStateException("Lembrete original não encontrado para esta entrada");
        }

        reminder.setConfirmed(true);
        reminder.setConfirmedAt(LocalDateTime.now());

        remindersRepository.save(reminder);

        entry.setFollowed(true);

        healthDiaryRepository.save(diary);

        return new ReminderCompletedResponse(
                diary.getId(),
                diary.getPatient().getNameComplete(),
                true,
                "Dieta confirmada com sucesso"
        );
    }


    @Transactional
    public ReminderCompletedResponse confirmarAtividade(Authentication authentication, UUID diaryId, UUID healthDiaryAtividadeId) {
        HealthDiary diary = healthDiaryRepository.findById(diaryId)
                .orElseThrow(() -> new EntityNotFoundException("Ativiade não encontrado"));

        Patient patient = diary.getPatient();
        if (patient == null) {
            throw new IllegalStateException("Paciente não associado ao diário");
        }

        User user = getUsuarioLogado(authentication);

        boolean isPatient = user.getIdUser().equals(patient.getIdUser());
        boolean isCaregiver = false;

        if (!isPatient) {
            Caregiver caregiver = getCuidadorLogado(authentication);
            isCaregiver = caregiverPatientRepository.existsByCaregiverAndPatientAndStatus(
                    caregiver,
                    patient,
                    VinculoStatus.ACEITO
            );
        }

        if (!isPatient && !isCaregiver) {
            throw new IllegalStateException("Sem permissão para confirmar entradas neste diário. " +
                    "Apenas o paciente dono ou cuidadores vinculados podem realizar esta ação.");
        }

        HealthDiaryActivity entry = diary.getActivities().stream()
                .filter(activity -> activity.getId().equals(healthDiaryAtividadeId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Entrada de atividade não encontrada"));

        if (entry.isCompleted()) {
            throw new IllegalStateException("Esta atividade já foi marcada como concluida");
        }

        entry.setCompleted(true);

        Reminders reminder = entry.getActivity() != null
                ? remindersRepository.findByActivityAndScheduledAtAndPatient(
                entry.getActivity(),
                entry.getTimeCompleted().atDate(diary.getDiaryDate()),
                diary.getPatient()
        ).orElse(null)
                : null;

        if (reminder == null) {
            throw new IllegalStateException("Lembrete original não encontrado para esta entrada");
        }

        reminder.setConfirmed(true);
        reminder.setConfirmedAt(LocalDateTime.now());

        remindersRepository.save(reminder);

        entry.setCompleted(true);

        healthDiaryRepository.save(diary);

        return new ReminderCompletedResponse(
                diary.getId(),
                diary.getPatient().getNameComplete(),
                true,
                "Atividade confirmada com sucesso"
        );
    }


    @Transactional
    public ReminderCompletedResponse confirmarSessao(Authentication authentication, UUID diaryId, UUID healthDiarySessaoId) {
        HealthDiary diary = healthDiaryRepository.findById(diaryId)
                .orElseThrow(() -> new EntityNotFoundException("Diário não encontrado"));

        Patient patient = diary.getPatient();
        if (patient == null) {
            throw new IllegalStateException("Paciente não associado ao diário");
        }

        User user = getUsuarioLogado(authentication);

        boolean isPatient = user.getIdUser().equals(patient.getIdUser());
        boolean isCaregiver = false;

        if (!isPatient) {
            Caregiver caregiver = getCuidadorLogado(authentication);
            isCaregiver = caregiverPatientRepository.existsByCaregiverAndPatientAndStatus(
                    caregiver,
                    patient,
                    VinculoStatus.ACEITO
            );
        }

        if (!isPatient && !isCaregiver) {
            throw new IllegalStateException("Sem permissão para confirmar entradas neste diário. " +
                    "Apenas o paciente dono ou cuidadores vinculados podem realizar esta ação.");
        }

        HealthDiarySession entry = diary.getSessions().stream()
                .filter(session -> session.getId().equals(healthDiarySessaoId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Entrada de sessao não encontrada"));

        if (entry.isAttended()) {
            throw new IllegalStateException("Esta sessao já foi marcada como concluida");
        }

        entry.setAttended(true);

        Reminders reminder = entry.getSession() != null
                ? remindersRepository.findBySessionAndScheduledAtAndPatient(
                entry.getSession(),
                entry.getTimeAttended().atDate(diary.getDiaryDate()),
                diary.getPatient()
        ).orElse(null)
                : null;

        if (reminder == null) {
            throw new IllegalStateException("Lembrete original não encontrado para esta entrada");
        }

        reminder.setConfirmed(true);
        reminder.setConfirmedAt(LocalDateTime.now());

        remindersRepository.save(reminder);

        entry.setAttended(true);

        healthDiaryRepository.save(diary);

        return new ReminderCompletedResponse(
                diary.getId(),
                diary.getPatient().getNameComplete(),
                true,
                "Sessão confirmada com sucesso"
        );
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


    @Transactional
    private HealthDiary getOrCreateDiary(Patient patient, LocalDate date, Caregiver caregiver) {
        return healthDiaryRepository
                .findByPatientAndDiaryDate(patient, date)
                .orElseGet(() -> {
                    HealthDiary diary = HealthDiary.builder()
                            .patient(patient)
                            .caregiver(caregiver)
                            .patientName(patient.getNameComplete())
                            .diaryDate(date)
                            .build();

                    System.out.println("Novo diary criado - medicines inicializado? " + (diary.getMedicines() != null));
                    System.out.println("Tamanho inicial de medicines: " + diary.getMedicines().size());

                    popularDiarioComLembretes(diary);

                    return healthDiaryRepository.save(diary);
                });
    }


    @Transactional
    private void popularDiarioComLembretes(HealthDiary diary) {

        UUID patientId = diary.getPatient().getIdUser();
        LocalDate today = diary.getDiaryDate();
        // Para evitar lembretes duplicados
        Set<String> deduplicationKeys = new HashSet<>();

        List<Reminders> reminders = remindersRepository.findByPatientIdUserAndActiveTrue(patientId);

        // 🔹 Cache local
        Map<UUID, Medicines> medicinesCache = new HashMap<>();
        Map<UUID, Diet> dietsCache = new HashMap<>();
        Map<UUID, Activities> activitiesCache = new HashMap<>();
        Map<UUID, Sessions> sessionsCache = new HashMap<>();

        // 🔹 Estratégias por tipo de lembrete
        Map<TipoLembrete, BiConsumer<LocalDateTime, Reminders>> handlers = Map.of(

                TipoLembrete.MEDICAMENTO, (occurrence, reminder) -> {
                    diary.setMedicines(
                            diary.getMedicines() != null ? diary.getMedicines() : new ArrayList<>()
                    );

                    // Proteção contra null + log para debug
                    if (reminder.getMedicine() == null) return;

                    Medicines medicine = medicinesCache.computeIfAbsent(
                            reminder.getMedicine().getIdMedicine(),
                            id -> medicinesRepository.findById(id).orElse(null)
                    );

                    if (medicine == null) return;

                    String key = "MEDICAMENTO:" + medicine.getIdMedicine() + ":" + occurrence.toLocalTime();
                    if (!deduplicationKeys.add(key)) return;

                    diary.getMedicines().add(
                            HealthDiaryMedicine.builder()
                                    .diary(diary)
                                    .medicine(medicine)
                                    .doseTaken(medicine.getDosage())
                                    .unityTaken(medicine.getUnity())
                                    .taken(false)
                                    .timeTaken(occurrence.toLocalTime())
                                    .note(medicine.getDescription())
                                    .build()
                    );
                },

                TipoLembrete.DIETA, (occurrence, reminder) -> {
                    diary.setDiets(
                            diary.getDiets() != null ? diary.getDiets() : new ArrayList<>()
                    );

                    if (reminder.getDiet() == null) return;

                    Diet diet = dietsCache.computeIfAbsent(
                            reminder.getDiet().getIdDiet(),
                            id -> dietRepository.findById(id).orElse(null)
                    );

                    if (diet == null) return;

                    String key = "DIETA:" + diet.getIdDiet() + ":" + today;
                    if (!deduplicationKeys.add(key)) return;

                    diary.getDiets().add(
                            HealthDiaryDiet.builder()
                                    .diary(diary)
                                    .diet(diet)
                                    .followed(false)
                                    .timeFollowed(occurrence.toLocalTime())
                                    .note(diet.getDescription())
                                    .build()
                    );
                },

                TipoLembrete.EXERCICIO, (occurrence, reminder) -> {
                    diary.setActivities(
                            diary.getActivities() != null ? diary.getActivities() : new ArrayList<>()
                    );

                    if (reminder.getActivity() == null) return;

                    Activities activity = activitiesCache.computeIfAbsent(
                            reminder.getActivity().getId(),
                            id -> activitiesRepository.findById(id).orElse(null)
                    );

                    if (activity == null) return;

                    String key = "EXERCICIO:" + activity.getId() + ":" + today;
                    if (!deduplicationKeys.add(key)) return;

                    diary.getActivities().add(
                            HealthDiaryActivity.builder()
                                    .diary(diary)
                                    .activity(activity)
                                    .completed(false)
                                    .timeCompleted(occurrence.toLocalTime())
                                    .note(activity.getDescription())
                                    .build()
                    );
                },

                TipoLembrete.SESSOES, (occurrence, reminder) -> {
                    diary.setSessions(
                            diary.getSessions() != null ? diary.getSessions() : new ArrayList<>()
                    );

                    if (reminder.getSession() == null) return;

                    Sessions session = sessionsCache.computeIfAbsent(
                            reminder.getSession().getId(),
                            id -> sessionsRepository.findById(id).orElse(null)
                    );

                    if (session == null) return;

                    String key = "SESSOES:" + session.getId() + ":" + today;
                    if (!deduplicationKeys.add(key)) return;

                    diary.getSessions().add(
                            HealthDiarySession.builder()
                                    .diary(diary)
                                    .session(session)
                                    .attended(false)
                                    .timeAttended(occurrence.toLocalTime())
                                    .build()
                    );
                }
        );

        for (Reminders reminder : reminders) {

            List<LocalDateTime> occurrences = calcularOcorrenciasDoDia(reminder, today);

            BiConsumer<LocalDateTime, Reminders> handler =
                    handlers.get(reminder.getType());

            if (handler == null) {
                System.out.println("→ Handler NÃO encontrado para: " + reminder.getType());
                continue;
            }

            for (LocalDateTime occurrence : occurrences) {
                handler.accept(occurrence, reminder);
            }
        }
    }


    private List<LocalDateTime> calcularOcorrenciasDoDia(Reminders reminder, LocalDate diaryDate) {
        List<LocalDateTime> ocorrencias = new ArrayList<>();
        LocalDateTime start = reminder.getScheduledAt();

        // Caso não recorrente
        if (!reminder.isRecurring()) {
            if (!start.toLocalDate().isAfter(diaryDate)) {
                ocorrencias.add(start);
            }
            return ocorrencias;
        }

        // Caso recorrente
        switch (reminder.getIntervalType()) {

            case HORA -> gerarOcorrenciasPorHora(reminder, diaryDate, ocorrencias);

            case DIARIO -> {
                if (verificarRecorrenciaDiaria(start, diaryDate)) {
                    ocorrencias.add(diaryDate.atTime(start.toLocalTime()));
                }
            }

            case SEMANAL -> {
                if (verificarRecorrenciaSemanal(start, diaryDate)) {
                    ocorrencias.add(diaryDate.atTime(start.toLocalTime()));
                }
            }

            case MENSAL -> {
                if (verificarRecorrenciaMensal(start, diaryDate)) {
                    ocorrencias.add(diaryDate.atTime(start.toLocalTime()));
                }
            }
        }

        return ocorrencias;
    }


    private void gerarOcorrenciasPorHora(Reminders reminder, LocalDate diaryDate,
            List<LocalDateTime> ocorrencias) {
        LocalDateTime occurrence = reminder.getScheduledAt();

        // Define quantas horas devem ser somadas entre uma ocorrência e outra do lembrete.
        int intervalo = reminder.getIntervalHours() != null ? reminder.getIntervalHours() : 1;

        while (!occurrence.toLocalDate().isAfter(diaryDate)) {

            if (occurrence.toLocalDate().equals(diaryDate)) {
                ocorrencias.add(occurrence);
            }

            occurrence = occurrence.plusHours(intervalo);

            if (occurrence.isAfter(diaryDate.atTime(23, 59))) {
                break;
            }
        }
    }


    private boolean verificarRecorrenciaDiaria(
            LocalDateTime start,
            LocalDate diaryDate
    ) {
        return !start.toLocalDate().isAfter(diaryDate);
    }


    private boolean verificarRecorrenciaSemanal(
            LocalDateTime start,
            LocalDate diaryDate
    ) {
        return !start.toLocalDate().isAfter(diaryDate)
                && start.getDayOfWeek() == diaryDate.getDayOfWeek();
    }


    private boolean verificarRecorrenciaMensal(
            LocalDateTime start,
            LocalDate diaryDate
    ) {
        return !start.toLocalDate().isAfter(diaryDate)
                && start.getDayOfMonth() == diaryDate.getDayOfMonth();
    }
}
