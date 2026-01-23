package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.domain.enums.RemetenteVinculo;
import com.SIMOD.SIMOD.domain.enums.VinculoStatus;
import com.SIMOD.SIMOD.domain.model.associacoes.CaregiverPatient;
import com.SIMOD.SIMOD.domain.model.associacoes.PatientProfessional;
import com.SIMOD.SIMOD.domain.model.cuidador.Caregiver;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import com.SIMOD.SIMOD.dto.vinculo.SolicitarVinculoRequest;
import com.SIMOD.SIMOD.repositories.PatientProfessionalRepository;
import com.SIMOD.SIMOD.repositories.PatientRepository;
import com.SIMOD.SIMOD.repositories.ProfessionalRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    // Regras comuns de todos os profissionais da saúde (métodos estáticos vazios – mantenha se precisar)
    public static void session() {}
    public static void linkPatient() {}
    public static void unlinkPatient() {}

    @Transactional
    public void solicitarVinculoPaciente(UUID professionalId, SolicitarVinculoRequest request) {
        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

        String cpf = request.cpf();

        Patient patient = patientRepository.findByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException("Não encontramos paciente com o CPF informado"));

        if (patientProfessionalRepository.existsByPatientAndProfessionalAndStatus(patient, professional, VinculoStatus.ACEITO)) {
            throw new IllegalStateException("Você já possui vínculo ativo com este paciente");
        }
        if (patientProfessionalRepository.existsByPatientAndProfessionalAndStatus(patient, professional, VinculoStatus.PENDENTE)) {
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
    }

    @Transactional(readOnly = true)
    public List<SolicitarVinculoRequest.VinculoResponse> listarPacientesAtivos(UUID professionalId) {
        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

        return patientProfessionalRepository.findByProfessionalAndStatus(professional, VinculoStatus.ACEITO)
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
    public List<SolicitarVinculoRequest.VinculoResponse> listarSolicitacoesPendentesPacientes(UUID professionalId) {
        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

        return patientProfessionalRepository.findByProfessionalAndStatus(professional, VinculoStatus.PENDENTE)
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
    public void aceitarSolicitacaoPaciente(UUID professionalId, UUID patientId) {
        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        PatientProfessional vinculo = patientProfessionalRepository.findByPatientAndProfessional(patient, professional)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada"));

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
    }

    @Transactional
    public void rejeitarSolicitacaoPaciente(UUID professionalId, UUID patientId, String motivo) {
        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        PatientProfessional vinculo = patientProfessionalRepository.findByPatientAndProfessional(patient, professional)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada"));

        if (vinculo.getStatus() != VinculoStatus.PENDENTE) {
            throw new IllegalStateException("Solicitação não está pendente");
        }

        vinculo.rejeitar(motivo);
        patientProfessionalRepository.save(vinculo);
    }

    @Transactional
    public void desfazerVinculoPaciente(UUID professionalId, UUID patientId) {
        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        PatientProfessional vinculo = patientProfessionalRepository
                .findByPatientAndProfessional(patient, professional)
                .orElseThrow(() -> new EntityNotFoundException("Vínculo não encontrado"));

        if (vinculo.getStatus() != VinculoStatus.ACEITO) {
            throw new IllegalStateException("Só é possível desfazer vínculos ativos");
        }

        vinculo.cancelar();
        patientProfessionalRepository.save(vinculo);
    }

}