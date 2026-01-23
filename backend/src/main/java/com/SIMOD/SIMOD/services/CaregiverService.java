package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.domain.enums.RemetenteVinculo;
import com.SIMOD.SIMOD.domain.enums.VinculoStatus;
import com.SIMOD.SIMOD.domain.model.associacoes.CaregiverPatient;
import com.SIMOD.SIMOD.domain.model.associacoes.PatientProfessional;
import com.SIMOD.SIMOD.domain.model.cuidador.Caregiver;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import com.SIMOD.SIMOD.dto.caregiver.CaregiverRequest;
import com.SIMOD.SIMOD.dto.vinculo.SolicitarVinculoRequest;
import com.SIMOD.SIMOD.repositories.CaregiverPatientRepository;
import com.SIMOD.SIMOD.repositories.CaregiverRepository;
import com.SIMOD.SIMOD.repositories.PatientRepository;
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
public class CaregiverService {

    private final CaregiverRepository caregiverRepository;
    private final PatientRepository patientRepository;
    private final CaregiverPatientRepository caregiverPatientRepository;

    public Caregiver criarCuidador(CaregiverRequest dado) {
        Caregiver novoCuidador = new Caregiver();
        novoCuidador.setCpf(dado.CPF());
        novoCuidador.setNameComplete(dado.nomeComplete());
        novoCuidador.setEmail(dado.email());
        novoCuidador.setPassword(dado.password());
        novoCuidador.setTelephone(dado.telephone());

        return caregiverRepository.save(novoCuidador);
    }

    @Transactional
    public void solicitarVinculoPaciente(UUID caregiverId, SolicitarVinculoRequest request) {
        Caregiver caregiver = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new EntityNotFoundException("Cuidador não encontrado"));

        String cpf = request.cpf();

        Patient patient = patientRepository.findByCpf(cpf)
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
    }

    @Transactional(readOnly = true)
    public List<SolicitarVinculoRequest.VinculoResponse> listarPacientesAtivos(UUID caregiverId) {
        Caregiver caregiver = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new EntityNotFoundException("Cuidador não encontrado"));

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
    public List<SolicitarVinculoRequest.VinculoResponse> listarSolicitacoesPendentesPacientes(UUID caregiverId) {
        Caregiver caregiver = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new EntityNotFoundException("Cuidador não encontrado"));

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
    public void aceitarSolicitacaoPaciente(UUID caregiverId, UUID patientId) {
        Caregiver caregiver = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new EntityNotFoundException("Cuidador não encontrado"));

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
    }

    @Transactional
    public void rejeitarSolicitacaoPaciente(UUID caregiverId, UUID patientId, String motivo) {
        Caregiver caregiver = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new EntityNotFoundException("Cuidador não encontrado"));

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        CaregiverPatient vinculo = caregiverPatientRepository.findByCaregiverAndPatient(caregiver, patient)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada"));

        if (vinculo.getStatus() != VinculoStatus.PENDENTE) {
            throw new IllegalStateException("Esta solicitação não está mais pendente");
        }

        vinculo.rejeitar(motivo);
        caregiverPatientRepository.save(vinculo);
    }

    @Transactional
    public void desfazerVinculoPaciente(UUID caregiverId, UUID patientId) {
        Caregiver caregiver = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new EntityNotFoundException("Cuidador não encontrado"));

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
    }
}