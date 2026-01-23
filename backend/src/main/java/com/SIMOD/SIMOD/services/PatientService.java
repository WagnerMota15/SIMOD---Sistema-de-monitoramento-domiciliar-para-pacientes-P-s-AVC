package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.domain.enums.RemetenteVinculo;
import com.SIMOD.SIMOD.domain.enums.VinculoStatus;
import com.SIMOD.SIMOD.domain.model.associacoes.CaregiverPatient;
import com.SIMOD.SIMOD.domain.model.associacoes.PatientProfessional;
import com.SIMOD.SIMOD.domain.model.cuidador.Caregiver;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import com.SIMOD.SIMOD.dto.patient.PatientRequest;
import com.SIMOD.SIMOD.dto.vinculo.SolicitarVinculoRequest;
import com.SIMOD.SIMOD.repositories.*;
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
public class PatientService {

    private final PatientRepository patientRepository;
    private final CaregiverRepository caregiverRepository;
    private final ProfessionalRepository professionalRepository;
    private final CaregiverPatientRepository caregiverPatientRepository;
    private final PatientProfessionalRepository patientProfessionalRepository;
    private final UserRepository userRepository;

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

    @Transactional
    public void solicitarVinculoCuidador(UUID patientId, SolicitarVinculoRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));
        String cpf = request.cpf();

        Caregiver caregiver = caregiverRepository.findByCpf(cpf)
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
    }

    @Transactional
    public void solicitarVinculoProfissional(UUID patientId, SolicitarVinculoRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        String cpf = request.cpf();

        Professional professional = professionalRepository.findByCpf(cpf)
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
    }

    // Listar cuidadores ativos
    @Transactional(readOnly = true)
    public List<SolicitarVinculoRequest.VinculoResponse> listarCuidadoresAtivos(UUID patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));
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

    // Listar profissionais ativos
    @Transactional(readOnly = true)
    public List<SolicitarVinculoRequest.VinculoResponse> listarProfissionaisAtivos(UUID patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

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
    public List<SolicitarVinculoRequest.VinculoResponse> listarSolicitacoesPendentesCuidadores(UUID patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

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
    public List<SolicitarVinculoRequest.VinculoResponse> listarSolicitacoesPendentesProfissionais(UUID patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

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
    public void aceitarSolicitacaoCuidador(UUID patientId, UUID caregiverId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

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
    }

    @Transactional
    public void rejeitarSolicitacaoCuidador(UUID patientId, UUID caregiverId, String motivo) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        Caregiver caregiver = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new EntityNotFoundException("Cuidador não encontrado"));

        CaregiverPatient vinculo = caregiverPatientRepository.findByCaregiverAndPatient(caregiver, patient)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada"));

        if (vinculo.getStatus() != VinculoStatus.PENDENTE) {
            throw new IllegalStateException("Esta solicitação não está mais pendente");
        }

        vinculo.rejeitar(motivo);
        caregiverPatientRepository.save(vinculo);
    }

    @Transactional
    public void aceitarSolicitacaoProfissional(UUID patientId, UUID professionalId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

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
    }

    @Transactional
    public void rejeitarSolicitacaoProfissional(UUID patientId, UUID professionalId, String motivo) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

        PatientProfessional vinculo = patientProfessionalRepository.findByPatientAndProfessional(patient, professional)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada"));

        if (vinculo.getStatus() != VinculoStatus.PENDENTE) {
            throw new IllegalStateException("Esta solicitação não está mais pendente");
        }

        vinculo.rejeitar(motivo);
        patientProfessionalRepository.save(vinculo);
    }

    @Transactional
    public void desfazerVinculoCuidador(UUID patientId, UUID caregiverId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        Caregiver caregiver = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new EntityNotFoundException("Cuidador não encontrado"));

        CaregiverPatient vinculo = caregiverPatientRepository
                .findByCaregiverAndPatient(caregiver, patient)
                .orElseThrow(() -> new EntityNotFoundException("Vínculo não encontrado"));

        if (vinculo.getStatus() != VinculoStatus.ACEITO) {
            throw new IllegalStateException("Só é possível desfazer vínculos ativos");
        }

        caregiverPatientRepository.save(vinculo);
    }

    @Transactional
    public void desfazerVinculoProfissional(UUID patientId, UUID professionalId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

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
    }


}