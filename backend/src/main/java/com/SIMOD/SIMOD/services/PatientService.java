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
import com.SIMOD.SIMOD.dto.patient.PatientRequest;
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
public class PatientService {

    private final PatientRepository patientRepository;
    private final CaregiverRepository caregiverRepository;
    private final ProfessionalRepository professionalRepository;
    private final CaregiverPatientRepository caregiverPatientRepository;
    private final PatientProfessionalRepository patientProfessionalRepository;
    private final UserRepository userRepository;
    private final NotificationsService notificationsService;
    private final SessionsRepository sessionsRepository;

    public Patient criarPaciente(PatientRequest dado) {
        Patient novoPaciente = new Patient();
        novoPaciente.setNameComplete(dado.nomeComplete());
        novoPaciente.setEmail(dado.email());
        novoPaciente.setCpf(dado.CPF());
        novoPaciente.setPassword(dado.password());
        novoPaciente.setTelephone(dado.telephone());
        // novoPaciente.setTipoAVC(dado.tipoAVC());
        return patientRepository.save(novoPaciente);
    }


    // ----- SISTEMA DE SESSÃO -----

    @Transactional
    public Sessions marcarSessaoParaPaciente(Authentication authentication, UUID professionalId, SessionsRequest request) {
        Patient patient = getPatientLogado(authentication);

        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

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
                .build();

        Sessions saved = sessionsRepository.save(session);

        NotificationsRequest notifProfissional = new NotificationsRequest(
                "Nova sessão marcada pelo paciente",
                "O paciente " + patient.getNameComplete() + " deseja marcar uma sessão com você" +
                        patient.getNameComplete() + " em " + request.dateTime() + ". Verifique e confirme.",
                "SESSAO_AGENDADA_POR_PACIENTE"
        );
        notificationsService.criarNotificacao(session.getProfessional().getIdUser(), notifProfissional);

        return saved;
    }

    @Transactional
    public void desmarcarSessao(Authentication authentication, UUID sessaoId) {
        Patient patient = getPatientLogado(authentication);

        Sessions session = sessionsRepository.findById(sessaoId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));

        if (!session.getPatient().getIdUser().equals(patient.getIdUser())) {
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
                "O paciente " + patient.getNameComplete() +
                        " desmarcou a sessão agendada para " + session.getDateTime() + ".",
                "SESSAO_DESMARCADA"
        );
        notificationsService.criarNotificacao(session.getProfessional().getIdUser(), notification);
    }

    @Transactional
    public Sessions confirmarSessao(Authentication authentication, UUID sessaoId) {
        Patient patient = getPatientLogado(authentication);

        Sessions session = sessionsRepository.findById(sessaoId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));

        if (!session.getPatient().getIdUser().equals(patient.getIdUser())) {
            throw new IllegalStateException("Você não tem permissão para confirmar esta sessão.");
        }

        if (session.getStatus() != SessionsStatus.AGENDADA) {
            throw new IllegalStateException("Sessão já foi " + session.getStatus().name().toLowerCase() + ".");
        }

        session.setStatus(SessionsStatus.CONFIRMADA);
        sessionsRepository.save(session);

        NotificationsRequest notification = new NotificationsRequest("Sessão confirmada",
                "O paciente " + patient.getNameComplete() + " confirmou sua sessão em " + session.getDateTime() + ".",
                "SESSAO_CONFIRMADA");
        notificationsService.criarNotificacao(session.getProfessional().getIdUser(), notification);
        notificationsService.criarNotificacao(session.getCaregiver().getIdUser(), notification);

        return session;
    }

    @Transactional
    public Sessions rejeitarSessao(Authentication authentication, UUID sessaoId, String motivo) {
        Patient patient = getPatientLogado(authentication);

        Sessions session = sessionsRepository.findById(sessaoId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));

        if (!session.getPatient().getIdUser().equals(patient.getIdUser())) {
            throw new IllegalStateException("Você não tem permissão para confirmar esta sessão.");
        }

        if (session.getStatus() != SessionsStatus.AGENDADA) {
            throw new IllegalStateException("Sessão já foi " + session.getStatus().name().toLowerCase() + ".");
        }

        session.setStatus(SessionsStatus.REJEITADA);
        session.setReasonChange(motivo);
        sessionsRepository.save(session);

        NotificationsRequest notification = new NotificationsRequest( "Sessão rejeitada",
                "O paciente rejeitou a sessão marcada para " + session.getDateTime() + ". Motivo: " + motivo,
                "SESSAO_REJEITADA");
        notificationsService.criarNotificacao(session.getProfessional().getIdUser(), notification);
        notificationsService.criarNotificacao(session.getCaregiver().getIdUser(), notification);

        return session;
    }

    @Transactional
    public Sessions cancelarSessao(Authentication authentication, UUID sessaoId, String motivo) {
        Patient patient = getPatientLogado(authentication);

        Sessions session = sessionsRepository.findById(sessaoId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));

        if (!session.getPatient().getIdUser().equals(patient.getIdUser())) {
            throw new IllegalStateException("Você não tem permissão para confirmar esta sessão.");
        }

        if (session.getStatus() == SessionsStatus.CANCELADA) {
            throw new IllegalStateException("Sessão já foi cancelada.");
        }

        session.setStatus(SessionsStatus.CANCELADA);
        session.setReasonChange(motivo);
        sessionsRepository.save(session);

        NotificationsRequest notification = new NotificationsRequest( "Sessão cancelada",
                "O paciente " + patient.getNameComplete() + " cancelou a sessão de " + session.getDateTime() + ". Motivo: " + motivo,
                "SESSAO_CANCELADA");
        notificationsService.criarNotificacao(session.getProfessional().getIdUser(), notification);
        notificationsService.criarNotificacao(session.getProfessional().getIdUser(), notification);

        return session;
    }

    @Transactional
    public Sessions reagendarSessao(Authentication authentication, UUID sessaoId, LocalDateTime novaDataHora, SessionsRequest request) {
        Patient patient = getPatientLogado(authentication);

        Sessions session = sessionsRepository.findById(sessaoId)
                .orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada"));

        if (!session.getPatient().getIdUser().equals(patient.getIdUser())) {
            throw new IllegalStateException("Você não tem permissão para confirmar esta sessão.");
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
                "O paciente reagendou sua sessão para " + novaDataHora + ".",
                "SESSAO_REAGENDADA");
        notificationsService.criarNotificacao(session.getCaregiver().getIdUser(), notification);
        notificationsService.criarNotificacao(session.getProfessional().getIdUser(), notification);

        return session;
    }

    @Transactional(readOnly = true)
    public Page<SessionsResponse> listarTodasMinhasSessoes(
            Authentication authentication,
            @Nullable SessionsStatus status,
            Pageable pageable) {

        Patient patient = getPatientLogado(authentication);

        Page<Sessions> page = sessionsRepository.findByPatientId(patient.getIdUser(), pageable);

        if (status != null) {
            page = page.map(s -> s.getStatus().equals(status) ? s : null);
        }

        return page.map(this::mapearParaResponse);
    }


    @Transactional(readOnly = true)
    public Page<SessionsResponse> listarSessoesAnteriores(Authentication authentication, Pageable pageable) {

        Patient patient = getPatientLogado(authentication);

        LocalDateTime agora = LocalDateTime.now();

        Page<Sessions> page = sessionsRepository.findByPatientId(patient.getIdUser(), pageable);

        List<Sessions> anteriores = page.getContent().stream()
                .filter(s -> s.getDateTime().isBefore(agora))
                .collect(Collectors.toList());

        return new PageImpl<>(anteriores.stream().map(this::mapearParaResponse).toList(), pageable, page.getTotalElements());
    }




    // ----- SISTEMA DE VÍNCULO -----
    @Transactional
    public void solicitarVinculoCuidador(Authentication authentication, SolicitarVinculoRequest request) {
        Patient patient = getPatientLogado(authentication);

        Caregiver caregiver = caregiverRepository.findByCpf(request.cpf())
                .orElseThrow(() -> new EntityNotFoundException("Não encontramos cuidador com o CPF informado"));

        if (caregiverPatientRepository.existsByCaregiverAndPatientAndStatus(caregiver, patient, VinculoStatus.ACEITO)) {
            throw new IllegalStateException("Você já possui vínculo ativo com este cuidador");
        }
        if (caregiverPatientRepository.existsByCaregiverAndPatientAndStatus(caregiver, patient, VinculoStatus.PENDENTE)) {
            throw new IllegalStateException("Já existe uma solicitação pendente para este cuidador");
        }

        CaregiverPatient vinculo = CaregiverPatient.builder()
                .caregiver(caregiver)
                .patient(patient)
                .status(VinculoStatus.PENDENTE)
                .dataSolicitacao(LocalDateTime.now())
                .observacao(request.observacao())
                .remetente(RemetenteVinculo.PACIENTE)
                .build();

        caregiverPatientRepository.save(vinculo);

        NotificationsRequest notificationRequest = new NotificationsRequest(
                "Nova solicitação de vínculo",
                "O paciente " + patient.getNameComplete() + " gostaria de se vincular a você.",
                "VINCULO_SOLICITADO"
        );

        notificationsService.criarNotificacao(caregiver.getIdUser(), notificationRequest);
        System.out.println("Notificação enviada");
    }

    @Transactional
    public void solicitarVinculoProfissional(Authentication authentication, SolicitarVinculoRequest request) {
        Patient patient = getPatientLogado(authentication);

        Professional professional = professionalRepository.findByCpf(request.cpf())
                .orElseThrow(() -> new EntityNotFoundException("Não encontramos profissional de saúde com o CPF informado"));

        if (patientProfessionalRepository.existsByPatientAndProfessionalAndStatus(patient, professional, VinculoStatus.ACEITO)) {
            throw new IllegalStateException("Você já possui vínculo ativo com este profissional");
        }
        if (patientProfessionalRepository.existsByPatientAndProfessionalAndStatus(patient, professional, VinculoStatus.PENDENTE)) {
            throw new IllegalStateException("Já existe uma solicitação pendente para este profissional");
        }

        PatientProfessional vinculo = PatientProfessional.builder()
                .patient(patient)
                .professional(professional)
                .status(VinculoStatus.PENDENTE)
                .dataSolicitacao(LocalDateTime.now())
                .observacao(request.observacao())
                .remetente(RemetenteVinculo.PACIENTE)
                .build();

        patientProfessionalRepository.save(vinculo);

        NotificationsRequest notificationRequest = new NotificationsRequest(
                "Nova solicitação de vínculo",
                "O paciente " + patient.getNameComplete() + " gostaria de se vincular a você.",
                "VINCULO_SOLICITADO"
        );

        notificationsService.criarNotificacao(professional.getIdUser(), notificationRequest);
    }

    @Transactional(readOnly = true)
    public List<SolicitarVinculoRequest.VinculoResponse> listarCuidadoresAtivos(Authentication authentication) {
        Patient patient = getPatientLogado(authentication);

        return caregiverPatientRepository.findByPatientAndStatus(patient, VinculoStatus.ACEITO)
                .stream()
                .map(v -> new SolicitarVinculoRequest.VinculoResponse(
                        v.getCaregiver().getCpf(),
                        v.getCaregiver().getNameComplete(),
                        v.getCaregiver().getEmail(),
                        v.getCaregiver().getTelephone(),
                        v.getDataSolicitacao(),
                        v.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SolicitarVinculoRequest.VinculoResponse> listarProfissionaisAtivos(Authentication authentication) {
        Patient patient = getPatientLogado(authentication);

        return patientProfessionalRepository.findByPatientAndStatus(patient, VinculoStatus.ACEITO)
                .stream()
                .map(v -> new SolicitarVinculoRequest.VinculoResponse(
                        v.getProfessional().getCpf(),
                        v.getProfessional().getNameComplete(),
                        v.getProfessional().getEmail(),
                        v.getProfessional().getTelephone(),
                        v.getDataSolicitacao(),
                        v.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

    // Listar solicitações pendentes de cuidadores
    @Transactional(readOnly = true)
    public List<SolicitarVinculoRequest.VinculoResponse> listarSolicitacoesPendentesCuidadores(Authentication authentication) {
        Patient patient = getPatientLogado(authentication);

        return caregiverPatientRepository.findByPatientAndStatus(patient, VinculoStatus.PENDENTE)
                .stream()
                .map(v -> new SolicitarVinculoRequest.VinculoResponse(
                        v.getCaregiver().getCpf(),
                        v.getCaregiver().getNameComplete(),
                        v.getCaregiver().getEmail(),
                        v.getCaregiver().getTelephone(),
                        v.getDataSolicitacao(),
                        v.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

    // Listar solicitações pendentes de profissionais
    @Transactional(readOnly = true)
    public List<SolicitarVinculoRequest.VinculoResponse> listarSolicitacoesPendentesProfissionais(Authentication authentication) {
        Patient patient = getPatientLogado(authentication);

        return patientProfessionalRepository.findByPatientAndStatus(patient, VinculoStatus.PENDENTE)
                .stream()
                .map(v -> new SolicitarVinculoRequest.VinculoResponse(
                        v.getProfessional().getCpf(),
                        v.getProfessional().getNameComplete(),
                        v.getProfessional().getEmail(),
                        v.getProfessional().getTelephone(),
                        v.getDataSolicitacao(),
                        v.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void aceitarSolicitacaoCuidador(Authentication authentication, UUID caregiverId) {
        Patient patient = getPatientLogado(authentication);
        ;

        Caregiver caregiver = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new EntityNotFoundException("Cuidador não encontrado"));

        CaregiverPatient vinculo = caregiverPatientRepository.findByCaregiverAndPatient(caregiver, patient)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada"));

        if (vinculo.getRemetente() == RemetenteVinculo.PACIENTE) {
            throw new IllegalStateException(
                    "Apenas o cuidador pode aceitar solicitações enviadas pelo paciente"
            );
        }

        if (vinculo.getStatus() != VinculoStatus.PENDENTE) {
            throw new IllegalStateException("Esta solicitação não está mais pendente");
        }

        vinculo.aceitar();
        caregiverPatientRepository.save(vinculo);

        NotificationsRequest notificationRequest = new NotificationsRequest(
                "Aceitação de vínculo",
                "O paciente " + patient.getNameComplete() + " aceitou sua solicitação",
                "VINCULO_ACEITADO"
        );

        notificationsService.criarNotificacao(caregiverId, notificationRequest);
    }

    @Transactional
    public void rejeitarSolicitacaoCuidador(Authentication authentication, UUID caregiverId, String motivo) {
        Patient patient = getPatientLogado(authentication);

        Caregiver caregiver = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new EntityNotFoundException("Cuidador não encontrado"));

        CaregiverPatient vinculo = caregiverPatientRepository.findByCaregiverAndPatient(caregiver, patient)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada"));

        if (vinculo.getStatus() != VinculoStatus.PENDENTE) {
            throw new IllegalStateException("Esta solicitação não está mais pendente");
        }

        vinculo.rejeitar(motivo);
        caregiverPatientRepository.save(vinculo);

        NotificationsRequest notificationRequest = new NotificationsRequest(
                "Rejeitação de vínculo",
                "O paciente " + patient.getNameComplete() + " rejeitou sua solicitação",
                "VINCULO_REJEITADO"
        );

        notificationsService.criarNotificacao(caregiverId, notificationRequest);
    }

    @Transactional
    public void aceitarSolicitacaoProfissional(Authentication authentication, UUID professionalId) {
        Patient patient = getPatientLogado(authentication);

        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

        PatientProfessional vinculo = patientProfessionalRepository.findByPatientAndProfessional(patient, professional)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada"));

        if (vinculo.getRemetente() == RemetenteVinculo.PACIENTE) {
            throw new IllegalStateException(
                    "Apenas o profissional pode aceitar solicitações enviadas pelo paciente"
            );
        }

        if (vinculo.getStatus() != VinculoStatus.PENDENTE) {
            throw new IllegalStateException("Esta solicitação não está mais pendente");
        }

        vinculo.aceitar();
        patientProfessionalRepository.save(vinculo);

        NotificationsRequest notificationRequest = new NotificationsRequest(
                "Aceitação de vínculo",
                "O paciente " + patient.getNameComplete() + " aceitou sua solicitação",
                "VINCULO_ACEITADO"
        );

        notificationsService.criarNotificacao(professionalId, notificationRequest);
    }

    @Transactional
    public void rejeitarSolicitacaoProfissional(Authentication authentication, UUID professionalId, String motivo) {
        Patient patient = getPatientLogado(authentication);
        ;

        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

        PatientProfessional vinculo = patientProfessionalRepository.findByPatientAndProfessional(patient, professional)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada"));

        if (vinculo.getStatus() != VinculoStatus.PENDENTE) {
            throw new IllegalStateException("Esta solicitação não está mais pendente");
        }

        vinculo.rejeitar(motivo);
        patientProfessionalRepository.save(vinculo);

        NotificationsRequest notificationRequest = new NotificationsRequest(
                "Rejeitação de vínculo",
                "O paciente " + patient.getNameComplete() + " rejeitou sua solicitação",
                "VINCULO_REJEITADO"
        );

        notificationsService.criarNotificacao(professionalId, notificationRequest);
    }

    @Transactional
    public void desfazerVinculoCuidador(Authentication authentication, UUID caregiverId) {
        Patient patient = getPatientLogado(authentication);

        Caregiver caregiver = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new EntityNotFoundException("Cuidador não encontrado"));

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
                "O paciente " + patient.getNameComplete() + " desfez o vínculo com você",
                "VINCULO_DESFEITO"
        );

        notificationsService.criarNotificacao(caregiverId, notificationRequest);
    }

    @Transactional
    public void desfazerVinculoProfissional(Authentication authentication, UUID professionalId) {
        Patient patient = getPatientLogado(authentication);

        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

        PatientProfessional vinculo = patientProfessionalRepository
                .findByPatientAndProfessional(patient, professional)
                .orElseThrow(() -> new EntityNotFoundException("Vínculo não encontrado"));

        if (vinculo.getStatus() != VinculoStatus.ACEITO) {
            throw new IllegalStateException("Só é possível desfazer vínculos ativos");
        }

        vinculo.cancelar();
        patientProfessionalRepository.save(vinculo);

        NotificationsRequest notificationRequest = new NotificationsRequest(
                "Desfez o vínculo",
                "O paciente " + patient.getNameComplete() + " desfez o vínculo com você",
                "VINCULO_DESFEITO"
        );

        notificationsService.criarNotificacao(professionalId, notificationRequest);
    }

    // Auxiliares
    private Patient getPatientLogado(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User usuario = userDetails.getUser();

        return (Patient) patientRepository.findByIdUser(usuario.getIdUser())
                .orElseThrow(() ->
                        new EntityNotFoundException("Paciente não encontrado para o usuário autenticado")
                );
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