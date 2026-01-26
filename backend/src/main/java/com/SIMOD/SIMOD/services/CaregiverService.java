package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.config.UserDetailsImpl;
import com.SIMOD.SIMOD.domain.enums.RemetenteVinculo;
import com.SIMOD.SIMOD.domain.enums.SessionsStatus;
import com.SIMOD.SIMOD.domain.enums.VinculoStatus;
import com.SIMOD.SIMOD.domain.model.associacoes.CaregiverPatient;
import com.SIMOD.SIMOD.domain.model.associacoes.PatientProfessional;
import com.SIMOD.SIMOD.domain.model.cuidador.Caregiver;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import com.SIMOD.SIMOD.domain.model.sessoes.Sessions;
import com.SIMOD.SIMOD.domain.model.usuario.User;
import com.SIMOD.SIMOD.dto.Messages.NotificationsRequest;
import com.SIMOD.SIMOD.dto.caregiver.CaregiverRequest;
import com.SIMOD.SIMOD.dto.plansTreatment.SessionsRequest;
import com.SIMOD.SIMOD.dto.plansTreatment.SessionsResponse;
import com.SIMOD.SIMOD.dto.vinculo.SolicitarVinculoRequest;
import com.SIMOD.SIMOD.repositories.*;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CaregiverService {

    private final CaregiverRepository caregiverRepository;
    private final PatientRepository patientRepository;
    private final CaregiverPatientRepository caregiverPatientRepository;
    private final ProfessionalRepository professionalRepository;
    private final PatientProfessionalRepository patientProfessionalRepository;
    private final SessionsRepository sessionsRepository;
    private final NotificationsService notificationsService;

    public Caregiver criarCuidador(CaregiverRequest dado) {
        Caregiver novoCuidador = new Caregiver();
        novoCuidador.setCpf(dado.CPF());
        novoCuidador.setNameComplete(dado.nomeComplete());
        novoCuidador.setEmail(dado.email());
        novoCuidador.setPassword(dado.password());
        novoCuidador.setTelephone(dado.telephone());

        return caregiverRepository.save(novoCuidador);
    }


    // ----- SISTEMA DE SESSÃO -----

    @Transactional
    public Sessions marcarSessaoParaPaciente(Authentication authentication, UUID patientId, UUID professionalId, SessionsRequest request) {
        Caregiver caregiver = getCaregiverLogado(authentication);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

        if (!patientProfessionalRepository.existsByPatientAndProfessionalAndStatus(
                patient, professional, VinculoStatus.ACEITO)) {
            throw new IllegalStateException("Não há vínculo ativo com este paciente.");
        }

        validarVinculoAtivoComPaciente(caregiver, patient);

        Sessions session = Sessions.builder()
                .dateTime(request.dateTime())
                .remote(request.remote())
                .status(SessionsStatus.AGENDADA)
                .place(request.place())
                .patient(patient)
                .professional(professional)
                .build();

        Sessions saved = sessionsRepository.save(session);

        NotificationsRequest notifPaciente = new NotificationsRequest(
                "Sessão marcada pelo cuidador",
                "O cuidador " + caregiver.getNameComplete() + " deseja marcar uma sessão com você" + request.dateTime() + ".",
                "SESSAO_AGENDADA_POR_CUIDADOR"
        );
        notificationsService.criarNotificacao(patient.getIdUser(), notifPaciente);

        NotificationsRequest notifProfissional = new NotificationsRequest(
                    "Nova sessão marcada pelo cuidador",
                "O cuidador " + caregiver.getNameComplete() + " marcou uma sessão para o paciente "
                        + patient.getNameComplete() + " em " + request.dateTime() + ". Acesse para confirmar ou ajustar.",
                    "SESSAO_AGENDADA_POR_CUIDADOR"
            );
            notificationsService.criarNotificacao(session.getProfessional().getIdUser(), notifProfissional);

        return saved;
    }

    @Transactional
    public void desmarcarSessao(Authentication authentication, UUID sessaoId) {
        Caregiver caregiver = getCaregiverLogado(authentication);

        Sessions session = sessionsRepository.findById(sessaoId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));

        if (!session.getCaregiver().getIdUser().equals(caregiver.getIdUser())) {
            throw new IllegalStateException("Você não tem permissão para desmarcar esta sessão.");
        }

        if (session.getDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Não é possível desmarcar sessões que já ocorreram.");
        }

        if (session.getStatus() == SessionsStatus.CANCELADA) {
            throw new IllegalStateException("Esta sessão já foi cancelada.");
        }

        session.setStatus(SessionsStatus.CANCELADA);
        sessionsRepository.save(session);

        NotificationsRequest notification = new NotificationsRequest(
                "Sessão desmarcada",
                "O cuidador " + caregiver.getNameComplete() +
                        " desmarcou a sessão agendada para " + session.getDateTime() + ".",
                "SESSAO_DESMARCADA"
        );
        notificationsService.criarNotificacao(session.getProfessional().getIdUser(), notification);
    }

    @Transactional
    public Sessions confirmarSessao(Authentication authentication, UUID sessaoId) {
        Caregiver caregiver = getCaregiverLogado(authentication);

        Sessions session = sessionsRepository.findById(sessaoId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));

        if (!session.getCaregiver().getIdUser().equals(caregiver.getIdUser())) {
            throw new IllegalStateException("Você não tem permissão para confirmar esta sessão.");
        }

        if (session.getStatus() != SessionsStatus.AGENDADA) {
            throw new IllegalStateException("Sessão já foi " + session.getStatus().name().toLowerCase() + ".");
        }

        session.setStatus(SessionsStatus.CONFIRMADA);
        sessionsRepository.save(session);

        NotificationsRequest notification = new NotificationsRequest("Sessão confirmada",
                "O Cuidador " + caregiver.getNameComplete() + " confirmou sua sessão em " + session.getDateTime() + ".",
                "SESSAO_CONFIRMADA");
        notificationsService.criarNotificacao(session.getProfessional().getIdUser(), notification);

        return session;
    }

    @Transactional
    public Sessions rejeitarSessao(Authentication authentication, UUID sessaoId, String motivo) {
        Caregiver caregiver = getCaregiverLogado(authentication);

        Sessions session = sessionsRepository.findById(sessaoId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));

        if (!session.getCaregiver().getIdUser().equals(caregiver.getIdUser())) {
            throw new IllegalStateException("Você não tem permissão para rejeitar esta sessão.");
        }

        if (session.getStatus() != SessionsStatus.AGENDADA) {
            throw new IllegalStateException("Sessão já foi " + session.getStatus().name().toLowerCase() + ".");
        }

        session.setStatus(SessionsStatus.REJEITADA);
        session.setReasonChange(motivo);
        sessionsRepository.save(session);

        NotificationsRequest notification = new NotificationsRequest( "Sessão rejeitada",
                "O cuidador rejeitou a sessão marcada para " + session.getDateTime() + ". Motivo: " + motivo,
                "SESSAO_REJEITADA");
        notificationsService.criarNotificacao(session.getProfessional().getIdUser(), notification);

        return session;
    }

    @Transactional
    public Sessions cancelarSessao(Authentication authentication, UUID sessaoId, String motivo) {
        Caregiver caregiver = getCaregiverLogado(authentication);

        Sessions session = sessionsRepository.findById(sessaoId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));

        if (!session.getCaregiver().getIdUser().equals(caregiver.getIdUser())) {
            throw new IllegalStateException("Você não tem permissão para cancelar esta sessão.");
        }

        if (session.getStatus() == SessionsStatus.CANCELADA) {
            throw new IllegalStateException("Sessão já foi cancelada.");
        }

        session.setStatus(SessionsStatus.CANCELADA);
        session.setReasonChange(motivo);
        sessionsRepository.save(session);

        NotificationsRequest notification = new NotificationsRequest( "Sessão cancelada",
                "O cuidador " + caregiver.getNameComplete() + " cancelou a sessão de " + session.getDateTime() + ". Motivo: " + motivo,
                "SESSAO_CANCELADA");
        notificationsService.criarNotificacao(session.getProfessional().getIdUser(), notification);

        return session;
    }

    @Transactional
    public Sessions reagendarSessao(Authentication authentication, UUID sessaoId, LocalDateTime novaDataHora, SessionsRequest request) {
        Caregiver caregiver = getCaregiverLogado(authentication);

        Sessions session = sessionsRepository.findById(sessaoId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));

        if (!session.getCaregiver().getIdUser().equals(caregiver.getIdUser())) {
            throw new IllegalStateException("Você não tem permissão para reagendar esta sessão.");
        }

        if (session.getStatus() == SessionsStatus.CANCELADA) {
            throw new IllegalStateException("Sessão não pode ser reagendada.");
        }

        session.setDateTime(novaDataHora);
        session.setRemote(request.remote());
        session.setPlace(request.place());
        session.setStatus(SessionsStatus.REAGENDADA);
        sessionsRepository.save(session);

        NotificationsRequest notification = new NotificationsRequest("Sessão reagendada",
                "O cuidador reagendou sua sessão para " + novaDataHora + ".",
                "SESSAO_REAGENDADA");
        notificationsService.criarNotificacao(session.getCaregiver().getIdUser(), notification);

        return session;
    }

    @Transactional(readOnly = true)
    public Page<SessionsResponse> listarTodasSessoesDoPaciente(
            Authentication authentication,
            UUID patientId,
            @Nullable SessionsStatus status,
            Pageable pageable) {

        Caregiver caregiver = getCaregiverLogado(authentication);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        validarVinculoAtivoComPaciente(caregiver, patient);

        Page<Sessions> page = sessionsRepository.findByPatientId(patientId, pageable);

        if (status != null) {
            page = page.map(s -> s.getStatus().equals(status) ? s : null);
        }

        return page.map(this::mapearParaResponse);
    }

    @Transactional(readOnly = true)
    public Page<SessionsResponse> listarSessoesAnterioresDoPaciente(
            Authentication authentication,
            UUID patientId,
            Pageable pageable) {

        Caregiver caregiver = getCaregiverLogado(authentication);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        validarVinculoAtivoComPaciente(caregiver, patient);

        LocalDateTime agora = LocalDateTime.now();

        Page<Sessions> page = sessionsRepository.findByPatientId(patientId, pageable);

        List<Sessions> anteriores = page.getContent().stream()
                .filter(s -> s.getDateTime().isBefore(agora))
                .collect(Collectors.toList());

        return new PageImpl<>(anteriores.stream().map(this::mapearParaResponse).toList(), pageable, page.getTotalElements());
    }




    // ----- SISTEMA DE VÍNCULO -----
    @Transactional
    public void solicitarVinculoPaciente(Authentication authentication, SolicitarVinculoRequest request) {
        Caregiver caregiver = getCaregiverLogado(authentication);

        Patient patient = patientRepository.findByCpf(request.cpf())
                .orElseThrow(() -> new EntityNotFoundException("Não encontramos paciente com o CPF informado"));

        if (caregiverPatientRepository.existsByCaregiverAndPatientAndStatus(caregiver, patient, VinculoStatus.ACEITO)) {
            throw new IllegalStateException("Você já possui vínculo ativo com este paciente");
        }
        if (caregiverPatientRepository.existsByCaregiverAndPatientAndStatus(caregiver, patient, VinculoStatus.PENDENTE)) {
            throw new IllegalStateException("Já existe uma solicitação pendente para este paciente");
        }

        CaregiverPatient vinculo = CaregiverPatient.builder()
                .caregiver(caregiver)
                .patient(patient)
                .status(VinculoStatus.PENDENTE)
                .dataSolicitacao(LocalDateTime.now())
                .observacao(request.observacao())
                .remetente(RemetenteVinculo.CUIDADOR)
                .build();

        caregiverPatientRepository.save(vinculo);

        NotificationsRequest notificationRequest = new NotificationsRequest(
                "Nova solicitação de vínculo",
                "O cuidador " + caregiver.getNameComplete() + " gostaria de cuidar de você.",
                "VINCULO_SOLICITADO"
        );

        notificationsService.criarNotificacao(patient.getIdUser(), notificationRequest);
    }

    @Transactional(readOnly = true)
    public List<SolicitarVinculoRequest.VinculoResponse> listarPacientesAtivos(Authentication authentication) {
        Caregiver caregiver = getCaregiverLogado(authentication);

        return caregiverPatientRepository.findByCaregiverAndStatus(caregiver, VinculoStatus.ACEITO)
                .stream()
                .map(v -> new SolicitarVinculoRequest.VinculoResponse(
                        v.getPatient().getCpf(),
                        v.getPatient().getNameComplete(),
                        v.getPatient().getEmail(),
                        v.getPatient().getTelephone(),
                        v.getDataSolicitacao(),
                        v.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SolicitarVinculoRequest.VinculoResponse> listarSolicitacoesPendentesPacientes(Authentication authentication) {
        Caregiver caregiver = getCaregiverLogado(authentication);

        return caregiverPatientRepository.findByCaregiverAndStatus(caregiver, VinculoStatus.PENDENTE)
                .stream()
                .map(v -> new SolicitarVinculoRequest.VinculoResponse(
                        v.getPatient().getCpf(),
                        v.getPatient().getNameComplete(),
                        v.getPatient().getEmail(),
                        v.getPatient().getTelephone(),
                        v.getDataSolicitacao(),
                        v.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void aceitarSolicitacaoPaciente(Authentication authentication, UUID patientId) {
        Caregiver caregiver = getCaregiverLogado(authentication);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        CaregiverPatient vinculo = caregiverPatientRepository.findByCaregiverAndPatient(caregiver, patient)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada"));

        if (vinculo.getRemetente() == RemetenteVinculo.CUIDADOR) {
            throw new IllegalStateException(
                    "Apenas o paciente pode aceitar solicitações enviadas pelo cuidador"
            );
        }

        if (vinculo.getStatus() != VinculoStatus.PENDENTE) {
            throw new IllegalStateException("Esta solicitação não está mais pendente");
        }

        vinculo.aceitar();
        caregiverPatientRepository.save(vinculo);

        NotificationsRequest notificationRequest = new NotificationsRequest(
                "Aceitação de vínculo",
                "O cuidador " + caregiver.getNameComplete() + " aceitou sua solicitação",
                "VINCULO_ACEITADO"
        );

        notificationsService.criarNotificacao(patientId, notificationRequest);
    }

    @Transactional
    public void rejeitarSolicitacaoPaciente(Authentication authentication, UUID patientId, String motivo) {
        Caregiver caregiver = getCaregiverLogado(authentication);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        CaregiverPatient vinculo = caregiverPatientRepository.findByCaregiverAndPatient(caregiver, patient)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada"));

        if (vinculo.getStatus() != VinculoStatus.PENDENTE) {
            throw new IllegalStateException("Esta solicitação não está mais pendente");
        }

        vinculo.rejeitar(motivo);
        caregiverPatientRepository.save(vinculo);

        NotificationsRequest notificationRequest = new NotificationsRequest(
                "Rejeitação de vínculo",
                "O cuidador " + caregiver.getNameComplete() + " rejeitou sua solicitação",
                "VINCULO_REJEITADO"
        );

        notificationsService.criarNotificacao(patientId, notificationRequest);
    }

    @Transactional
    public void desfazerVinculoPaciente(Authentication authentication, UUID patientId) {
        Caregiver caregiver = getCaregiverLogado(authentication);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        CaregiverPatient vinculo = caregiverPatientRepository
                .findByCaregiverAndPatient(caregiver, patient)
                .orElseThrow(() -> new EntityNotFoundException("Vínculo não encontrado"));

        if (vinculo.getStatus() != VinculoStatus.ACEITO) {
            throw new IllegalStateException("Só é possível desfazer vínculos ativos");
        }

        vinculo.cancelar();
        caregiverPatientRepository.save(vinculo);

        NotificationsRequest notificationRequest = new NotificationsRequest(
                "Desfez o vínculo",
                "O cuidador " + caregiver.getNameComplete() + " desfez o vínculo com você",
                "VINCULO_DESFEITO"
        );

        notificationsService.criarNotificacao(patientId, notificationRequest);
    }


    // Auxiliares
    private Caregiver getCaregiverLogado(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User usuario = userDetails.getUser();

        return (Caregiver) caregiverRepository.findByIdUser(usuario.getIdUser())
                .orElseThrow(() ->
                        new EntityNotFoundException("Cuidador não encontrado para o usuário autenticado")
                );
    }

    private void validarVinculoAtivoComPaciente(Caregiver caregiver, Patient patient) {
        if (!caregiverPatientRepository.existsByCaregiverAndPatientAndStatus(
                caregiver, patient, VinculoStatus.ACEITO)) {
            throw new IllegalStateException("Você não tem vínculo ativo com este paciente.");
        }
    }

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