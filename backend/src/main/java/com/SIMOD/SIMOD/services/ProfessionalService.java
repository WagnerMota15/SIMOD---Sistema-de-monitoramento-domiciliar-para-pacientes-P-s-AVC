package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.config.UserDetailsImpl;
import com.SIMOD.SIMOD.domain.enums.*;
import com.SIMOD.SIMOD.domain.model.associacoes.CaregiverPatient;
import com.SIMOD.SIMOD.domain.model.associacoes.PatientProfessional;
import com.SIMOD.SIMOD.domain.model.cuidador.Caregiver;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import com.SIMOD.SIMOD.domain.model.sessoes.Sessions;
import com.SIMOD.SIMOD.domain.model.usuario.User;
import com.SIMOD.SIMOD.dto.Messages.NotificationsRequest;
import com.SIMOD.SIMOD.dto.plansTreatment.SessionsRequest;
import com.SIMOD.SIMOD.dto.plansTreatment.SessionsResponse;
import com.SIMOD.SIMOD.dto.vinculo.SolicitarVinculoRequest;
import com.SIMOD.SIMOD.repositories.*;
import com.SIMOD.SIMOD.services.firebase.NotificationFacadeService;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;
    private final PatientRepository patientRepository;
    private final PatientProfessionalRepository patientProfessionalRepository;
    private final CaregiverPatientRepository caregiverPatientRepository;
    private final SessionsRepository sessionsRepository;
    private final NotificationFacadeService notificationFacadeService;


    // ----- SISTEMA DE SESSÃO -----
    @Transactional
    public Sessions marcarSessao(Authentication authentication, UUID patientId, SessionsRequest request) {
        Professional professional = getProfessionalLogado(authentication);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        if (!patientProfessionalRepository.existsByPatientAndProfessionalAndStatus(
                patient, professional, VinculoStatus.ACEITO)) {
            throw new IllegalStateException("Não há vínculo ativo com este paciente.");
        }

        Sessions session = Sessions.builder()
                .dateTime(request.dateTime())
                .remote(request.remote())
                .status(SessionsStatus.AGENDADA)
                .place(request.place())
                .patient(patient)
                .professional(professional)
                .criadoPorTipo(professional.getRole())
                .build();

        Sessions saved = sessionsRepository.save(session);


        NotificationsRequest notifPaciente = new NotificationsRequest(
                "Nova sessão agendada",
                "O profissional " + professional.getNameComplete() +
                        " marcou uma sessão para você em " + request.dateTime() +
                        (request.remote() ? " (remota)" : " (presencial em " + request.place() + ")"),
                TipoNotificacao.INFO
        );
        notificationFacadeService.notify(patient.getIdUser(), notifPaciente);

        notificarTodosCuidadores(patient,
                "Nova sessão agendada do paciente",
                "O profissional " + professional.getNameComplete() +
                        " marcou uma sessão para o paciente " + patient.getNameComplete() +
                        " em " + request.dateTime() +
                        (request.remote() ? " (remota)" : " (presencial em " + request.place() + ")"),
                TipoNotificacao.INFO
        );

        return saved;
    }


    @Transactional
    public void desmarcarSessao(Authentication authentication, UUID sessaoId) {
        Professional professional = getProfessionalLogado(authentication);

        Sessions session = sessionsRepository.findById(sessaoId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));

        if (!session.getProfessional().getIdUser().equals(professional.getIdUser())) {
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

        NotificationsRequest notifPaciente = new NotificationsRequest(
                "Sessão desmarcada",
                "O profissional " + professional.getNameComplete() +
                        " desmarcou a sessão agendada para " + session.getDateTime() + ".",
                TipoNotificacao.INFO
        );
        notificationFacadeService.notify(session.getPatient().getIdUser(), notifPaciente);

        notificarTodosCuidadores(session.getPatient(),
                "Sessão do paciente desmarcada",
                "O profissional " + professional.getNameComplete() +
                        " desmarcou a sessão do paciente " + session.getPatient().getNameComplete() +
                        " agendada para " + session.getDateTime() + ".",
                TipoNotificacao.INFO
        );
    }


    @Transactional
    public Sessions confirmarSessao(Authentication authentication, UUID sessaoId) {
        Professional professional = getProfessionalLogado(authentication);

        Sessions session = sessionsRepository.findById(sessaoId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));

        if (!session.getProfessional().getIdUser().equals(professional.getIdUser())) {
            throw new IllegalStateException("Você não tem permissão para confirmar esta sessão.");
        }

        if (session.getStatus() != SessionsStatus.AGENDADA) {
            throw new IllegalStateException("Sessão já foi " + session.getStatus().name().toLowerCase() + ".");
        }

        session.setStatus(SessionsStatus.CONFIRMADA);
        sessionsRepository.save(session);

        NotificationsRequest notifPaciente = new NotificationsRequest(
                "Sessão confirmada",
                "O profissional " + professional.getNameComplete() +
                        " confirmou sua sessão em " + session.getDateTime() + ".",
                TipoNotificacao.INFO
        );
        notificationFacadeService.notify(session.getPatient().getIdUser(), notifPaciente);

        notificarTodosCuidadores(session.getPatient(),
                "Sessão do paciente confirmada",
                "O profissional " + professional.getNameComplete() +
                        " confirmou a sessão do paciente " + session.getPatient().getNameComplete() +
                        " em " + session.getDateTime() + ".",
                TipoNotificacao.INFO
        );;

        return session;
    }


    @Transactional
    public Sessions rejeitarSessao(Authentication authentication, UUID sessaoId, String motivo) {
        Professional professional = getProfessionalLogado(authentication);

        Sessions session = sessionsRepository.findById(sessaoId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));

        if (!session.getProfessional().getIdUser().equals(professional.getIdUser())) {
            throw new IllegalStateException("Você não tem permissão para rejeitar esta sessão.");
        }

        if (session.getStatus() != SessionsStatus.AGENDADA && session.getStatus() != SessionsStatus.REAGENDADA) {
            throw new IllegalStateException("Sessão já foi " + session.getStatus().name().toLowerCase() + ".");
        }

        session.setStatus(SessionsStatus.REJEITADA);
        session.setReasonChange(motivo);
        sessionsRepository.save(session);

        NotificationsRequest notifPaciente = new NotificationsRequest(
                "Sessão rejeitada",
                "O profissional rejeitou a sessão marcada para " + session.getDateTime() + ". Motivo: " + motivo,
                TipoNotificacao.INFO
        );
        notificationFacadeService.notify(session.getPatient().getIdUser(), notifPaciente);

        notificarTodosCuidadores(session.getPatient(),
                "Sessão do paciente rejeitada",
                "O profissional rejeitou a sessão do paciente " + session.getPatient().getNameComplete() +
                        " marcada para " + session.getDateTime() + ". Motivo: " + motivo,
                TipoNotificacao.INFO
        );

        return session;
    }


    @Transactional
    public Sessions cancelarSessao(Authentication authentication, UUID sessaoId, String motivo) {
        Professional professional = getProfessionalLogado(authentication);

        Sessions session = sessionsRepository.findById(sessaoId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));

        if (!session.getProfessional().getIdUser().equals(professional.getIdUser())) {
            throw new IllegalStateException("Você não tem permissão para cancelar esta sessão.");
        }

        if (session.getStatus() == SessionsStatus.CANCELADA) {
            throw new IllegalStateException("Sessão já foi cancelada.");
        }

        session.setStatus(SessionsStatus.CANCELADA);
        session.setReasonChange(motivo);
        sessionsRepository.save(session);

        NotificationsRequest notifPaciente = new NotificationsRequest(
                "Sessão cancelada",
                "O profissional " + professional.getNameComplete() +
                        " cancelou a sessão de " + session.getDateTime() + ". Motivo: " + motivo,
                TipoNotificacao.INFO
        );
        notificationFacadeService.notify(session.getPatient().getIdUser(), notifPaciente);

        notificarTodosCuidadores(session.getPatient(),
                "Sessão do paciente cancelada",
                "O profissional " + professional.getNameComplete() +
                        " cancelou a sessão do paciente " + session.getPatient().getNameComplete() +
                        " marcada para " + session.getDateTime() + ". Motivo: " + motivo,
                TipoNotificacao.INFO
        );

        return session;
    }


    @Transactional
    public Sessions reagendarSessao(Authentication authentication, UUID sessaoId, LocalDateTime novaDataHora, SessionsRequest request) {
        Professional professional = getProfessionalLogado(authentication);

        Sessions session = sessionsRepository.findById(sessaoId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));

        if (!session.getProfessional().getIdUser().equals(professional.getIdUser())) {
            throw new IllegalStateException("Você não tem permissão para reagendar esta sessão.");
        }

        if (session.getStatus() == SessionsStatus.CANCELADA) {
            throw new IllegalStateException("Sessão não pode ser reagendada.");
        }

        session.setDateTime(novaDataHora);
        session.setRemote(request.remote());
        session.setPlace(request.place());
        session.setReasonChange(request.reasonChange());
        session.setStatus(SessionsStatus.REAGENDADA);
        sessionsRepository.save(session);

        NotificationsRequest notifPaciente = new NotificationsRequest(
                "Sessão reagendada",
                "O profissional reagendou sua sessão para " + novaDataHora + ".",
                TipoNotificacao.INFO
        );
        notificationFacadeService.notify(session.getPatient().getIdUser(), notifPaciente);

        notificarTodosCuidadores(session.getPatient(),
                "Sessão do paciente reagendada",
                "O profissional " + professional.getNameComplete() +
                        " reagendou a sessão do paciente " + session.getPatient().getNameComplete() +
                        " para " + novaDataHora + ".",
                TipoNotificacao.INFO
        );

        return session;
    }

    @Transactional(readOnly = true)
    public Page<SessionsResponse> listarTodasMinhasSessoes(
            Authentication authentication,
            @Nullable SessionsStatus status,
            Pageable pageable) {

        Professional professional = getProfessionalLogado(authentication);

        Page<Sessions> page = sessionsRepository.findByProfessionalIdUser(professional.getIdUser(), pageable);

        if (status != null) {
            page = page.map(s -> s.getStatus().equals(status.name()) ? s : null);
        }

        return page.map(this::mapearParaResponse);
    }

    @Transactional(readOnly = true)
    public Page<SessionsResponse> listarSessoesAnteriores(Authentication authentication, Pageable pageable) {
        Professional professional = getProfessionalLogado(authentication);

        LocalDateTime agora = LocalDateTime.now();

        Page<Sessions> page = sessionsRepository.findByProfessionalIdUser(professional.getIdUser(), pageable);

        List<Sessions> anteriores = page.getContent().stream()
                .filter(s -> s.getDateTime().isBefore(agora))
                .collect(Collectors.toList());

        return new PageImpl<>(
                anteriores.stream().map(this::mapearParaResponse).toList(),
                pageable,
                page.getTotalElements()
        );
    }


    // ----- SISTEMA DE VÍNCULO -----
    @Transactional
    public void solicitarVinculoPaciente(Authentication authentication, SolicitarVinculoRequest request) {
        Professional professional = getProfessionalLogado(authentication);

        Patient patient = patientRepository.findByCpf(request.cpf())
                .orElseThrow(() ->
                        new EntityNotFoundException("Não encontramos paciente com o CPF informado")
                );

        if (patientProfessionalRepository
                .existsByPatientAndProfessionalAndStatus(
                        patient, professional, VinculoStatus.ACEITO
                )) {
            throw new IllegalStateException("Você já possui vínculo ativo com este paciente");
        }

        if (patientProfessionalRepository
                .existsByPatientAndProfessionalAndStatus(
                        patient, professional, VinculoStatus.PENDENTE
                )) {
            throw new IllegalStateException("Já existe uma solicitação pendente para este paciente");
        }

        PatientProfessional vinculo = PatientProfessional.builder()
                .patient(patient)
                .professional(professional)
                .dataSolicitacao(LocalDateTime.now())
                .status(VinculoStatus.PENDENTE)
                .observacao(request.observacao())
                .remetente(RemetenteVinculo.PROFISSIONAL)
                .build();

        patientProfessionalRepository.save(vinculo);

        NotificationsRequest notificationRequest = new NotificationsRequest(
                "Nova solicitação de vínculo",
                "O profissional " + professional.getNameComplete() + " gostaria de se vincular a você.",
                TipoNotificacao.INFO
        );

        notificationFacadeService.notify(patient.getIdUser(), notificationRequest);
    }


    @Transactional(readOnly = true)
    public List<SolicitarVinculoRequest.VinculoResponse> listarPacientesAtivos(Authentication authentication) {
        Professional professional = getProfessionalLogado(authentication);

        return patientProfessionalRepository
                .findByProfessionalAndStatus(professional, VinculoStatus.ACEITO)
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
        Professional professional = getProfessionalLogado(authentication);

        return patientProfessionalRepository
                .findByProfessionalAndStatus(professional, VinculoStatus.PENDENTE)
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
        Professional professional = getProfessionalLogado(authentication);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Paciente não encontrado")
                );

        PatientProfessional vinculo = patientProfessionalRepository
                .findByPatientAndProfessional(patient, professional)
                .orElseThrow(() ->
                        new EntityNotFoundException("Solicitação não encontrada")
                );

        if (vinculo.getRemetente() == RemetenteVinculo.PROFISSIONAL) {
            throw new IllegalStateException(
                    "Apenas o paciente pode aceitar solicitações enviadas pelo profissional"
            );
        }

        if (vinculo.getStatus() != VinculoStatus.PENDENTE) {
            throw new IllegalStateException("Solicitação não está pendente");
        }

        vinculo.aceitar();
        patientProfessionalRepository.save(vinculo);

        NotificationsRequest notificationRequest = new NotificationsRequest(
                "Aceitação de vínculo",
                "O profissional " + professional.getNameComplete() + " aceitou sua solicitação",
                TipoNotificacao.INFO
        );

        notificationFacadeService.notify(patientId, notificationRequest);
    }


    @Transactional
    public void rejeitarSolicitacaoPaciente(Authentication authentication, UUID patientId, String motivo) {
        Professional professional = getProfessionalLogado(authentication);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Paciente não encontrado")
                );

        PatientProfessional vinculo = patientProfessionalRepository
                .findByPatientAndProfessional(patient, professional)
                .orElseThrow(() ->
                        new EntityNotFoundException("Solicitação não encontrada")
                );

        if (vinculo.getStatus() != VinculoStatus.PENDENTE) {
            throw new IllegalStateException("Solicitação não está pendente");
        }

        vinculo.rejeitar(motivo);
        patientProfessionalRepository.save(vinculo);

        NotificationsRequest notificationRequest = new NotificationsRequest(
                "Rejeitação de vínculo",
                "O profissional " + professional.getNameComplete() + " rejeitou sua solicitação",
                TipoNotificacao.INFO
        );

        notificationFacadeService.notify(patientId, notificationRequest);
    }


    @Transactional
    public void desfazerVinculoPaciente(Authentication authentication, UUID patientId) {
        Professional professional = getProfessionalLogado(authentication);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Paciente não encontrado")
                );

        PatientProfessional vinculo = patientProfessionalRepository
                .findByPatientAndProfessional(patient, professional)
                .orElseThrow(() ->
                        new EntityNotFoundException("Vínculo não encontrado")
                );

        if (vinculo.getStatus() != VinculoStatus.ACEITO) {
            throw new IllegalStateException("Só é possível desfazer vínculos ativos");
        }

        vinculo.cancelar();
        patientProfessionalRepository.save(vinculo);

        NotificationsRequest notificationRequest = new NotificationsRequest(
                "Desfez o vínculo",
                "O profissional " + professional.getNameComplete() + " desfez o vínculo com você",
                TipoNotificacao.INFO
        );

        notificationFacadeService.notify(patientId, notificationRequest);
    }


    // Auxiliar
    private Professional getProfessionalLogado(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User usuario = userDetails.getUser();

        return (Professional) professionalRepository.findByIdUser(usuario.getIdUser())
                .orElseThrow(() ->
                        new EntityNotFoundException("Profissional não encontrado para o usuário autenticado")
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