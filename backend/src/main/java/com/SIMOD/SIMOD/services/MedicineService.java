package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.config.UserDetailsImpl;
import com.SIMOD.SIMOD.domain.enums.*;
import com.SIMOD.SIMOD.domain.model.associacoes.CaregiverPatient;
import com.SIMOD.SIMOD.domain.model.associacoes.PatientProfessional;
import com.SIMOD.SIMOD.domain.model.cuidador.Caregiver;
import com.SIMOD.SIMOD.domain.model.medicamentos.Medicines;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import com.SIMOD.SIMOD.domain.model.usuario.User;
import com.SIMOD.SIMOD.dto.Messages.NotificationsRequest;
import com.SIMOD.SIMOD.dto.plansTreatment.MedicinesRequest;
import com.SIMOD.SIMOD.dto.plansTreatment.MedicinesResponse;
import com.SIMOD.SIMOD.repositories.*;
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
public class MedicineService {
    private final ProfessionalRepository professionalRepository;
    private final PatientRepository patientRepository;
    private final CaregiverRepository caregiverRepository;
    private final PatientProfessionalRepository patientProfessionalRepository;
    private final CaregiverPatientRepository caregiverPatientRepository;
    private final MedicinesRepository medicinesRepository;
    private final NotificationsService notificationsService;


    @Transactional
    public Medicines prescreverMedicamento(Authentication authentication, UUID patientId, MedicinesRequest request){
        Professional professional = getProfessionalLogado(authentication);

        if (professional.getRole() != Role.MEDICO) {
            throw new IllegalStateException("Apenas medicos podem prescrever medicamento");
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        if (!patientProfessionalRepository.existsByPatientAndProfessionalAndStatus(
                patient, professional, VinculoStatus.ACEITO)) {
            throw new IllegalStateException("Não há vínculo ativo com este paciente");
        }

        Medicines medicine = Medicines.builder()
                .name(request.name())
                .dosage(request.dosagem())
                .description(request.description())
                .status(Status.ATIVA)
                .frequency(request.frequency())
                .unity(request.unity())
                .patient(patient)
                .professional(professional)
                .build();

        Medicines savedMedicine = medicinesRepository.save(medicine);

        NotificationsRequest notifPaciente = new NotificationsRequest(
                "Nova medicamento prescrito",
                "O medico " + professional.getNameComplete() +
                        " prescreveu um novo medicamento para você.",
                "MEDICAMENTO_PRESCRITO"
        );
        notificationsService.criarNotificacao(patient.getIdUser(), notifPaciente);

        notificarTodosCuidadores(patient,
                "Novo medicamento prescrito para o paciente",
                "O medico " + professional.getNameComplete() +
                        " prescreveu um medicamento para o paciente " + patient.getNameComplete(),
                "MEDICAMENTO_PRESCRITO"
        );

        return savedMedicine;
    }

    @Transactional
    public MedicinesResponse editarMedicamento(Authentication authentication, UUID dietId, MedicinesRequest request) {
        Professional professional = getProfessionalLogado(authentication);

        Medicines medicine = medicinesRepository.findById(dietId)
                .orElseThrow(() -> new EntityNotFoundException("Medicamento não encontrada"));

        if (!medicine.getProfessional().getIdUser().equals(professional.getIdUser())) {
            throw new IllegalStateException("Você não pode editar esta receita");
        }

        if (professional.getRole() != Role.MEDICO) {
            throw new IllegalStateException("Apenas medicos podem editar receitas");
        }

        if (medicine.getStatus() != Status.ATIVA) {
            throw new IllegalStateException("Apenas medicamentos ativos podem ser editadas");
        }

        medicine.setDosage(request.dosagem());
        medicine.setUnity(request.unity());
        medicine.setFrequency(request.frequency());
        medicine.setDescription(request.description());

        Medicines updated = medicinesRepository.save(medicine);

        NotificationsRequest notifPaciente = new NotificationsRequest(
                "Medicamento editado",
                "O medico " + professional.getNameComplete() +
                        " editou um medicamento.",
                "MEDICAMENTO_EDITADO"
        );
        notificationsService.criarNotificacao(medicine.getPatient().getIdUser(), notifPaciente);

        notificarTodosCuidadores(medicine.getPatient(),
                "Medicamento editado para o paciente",
                "O medicamento " + professional.getNameComplete() +
                        " editou um medicamento do paciente " + medicine.getPatient().getNameComplete(),
                "MEDICAMENTO_EDITADO"
        );

        return toResponse(updated);
    }

    @Transactional
    public void inativarMedicamento(Authentication authentication, UUID medicineId) {
        Professional professional = getProfessionalLogado(authentication);

        Medicines medicine = medicinesRepository.findById(medicineId)
                .orElseThrow(() -> new EntityNotFoundException("Medicamento não encontrado"));

        if (!medicine.getProfessional().getIdUser().equals(professional.getIdUser())) {
            throw new IllegalStateException("Você não pode inativar este medicamento");
        }

        if (professional.getRole() != Role.MEDICO) {
            throw new IllegalStateException("Apenas medicos podem inativar esta receita");
        }

        if (medicine.getStatus() == Status.INATIVA) {
            throw new IllegalStateException("Este medicamento já está inativo");
        }

        medicine.setStatus(Status.INATIVA);
        medicinesRepository.save(medicine);

        NotificationsRequest notifPaciente = new NotificationsRequest(
                "Medicamento inativado",
                "O medico " + professional.getNameComplete() +
                        " inativou um medicamento.",
                "MEDICAMENTO_INATIVADO"
        );
        notificationsService.criarNotificacao(medicine.getPatient().getIdUser(), notifPaciente);

        notificarTodosCuidadores(medicine.getPatient(),
                "Medicamento inativado para o paciente",
                "O medico " + professional.getNameComplete() +
                        " inativou um medicamento do paciente " + medicine.getPatient().getNameComplete(),
                "MEDICAMENTO_INATIVADO"
        );
    }

    @Transactional(readOnly = true)
    public Page<MedicinesResponse> listarMedicamentos(Authentication authentication, Pageable pageable) {
        User user = getUsuarioLogado(authentication);

        return switch (user.getRole()) {
            case PACIENTE -> medicinesRepository
                    .findByPatientIdUserOrderByCreatedAtDesc(user.getIdUser(), pageable)
                    .map(this::toResponse);

            case MEDICO -> {
                Professional professional = getProfessionalLogado(authentication);

                List<PatientProfessional> vinculos = patientProfessionalRepository.findByProfessionalAndStatus(
                        professional, VinculoStatus.ACEITO);

                List<Patient> pacientes = vinculos.stream()
                        .map(PatientProfessional::getPatient)
                        .toList();

                yield medicinesRepository
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

                yield medicinesRepository
                        .findByPatientInOrderByCreatedAtDesc(pacientes, pageable)
                        .map(this::toResponse);
            }
            default -> throw new IllegalStateException("Perfil não autorizado");
        };
    }

    @Transactional(readOnly = true)
    public Page<MedicinesResponse> listarMedicamentosAtivos(Authentication authentication, Pageable pageable) {
        User user = getUsuarioLogado(authentication);

        return switch (user.getRole()) {
            case PACIENTE -> medicinesRepository
                    .findByPatientIdUserAndStatusOrderByCreatedAtDesc(
                            user.getIdUser(),
                            Status.ATIVA,
                            pageable
                    )
                    .map(this::toResponse);

            case MEDICO -> {
                Professional professional = getProfessionalLogado(authentication);

                List<Patient> pacientes = patientProfessionalRepository
                        .findByProfessionalAndStatus(professional, VinculoStatus.ACEITO)
                        .stream()
                        .map(PatientProfessional::getPatient)
                        .toList();

                yield medicinesRepository.findByPatientInAndStatusOrderByCreatedAtDesc(
                        pacientes, Status.ATIVA, pageable).map(this::toResponse);
            }

            case CUIDADOR -> {
                Caregiver caregiver = getCuidadorLogado(authentication);

                List<Patient> pacientes = caregiverPatientRepository
                        .findByCaregiverAndStatus(caregiver, VinculoStatus.ACEITO)
                        .stream()
                        .map(CaregiverPatient::getPatient)
                        .toList();

                yield medicinesRepository.findByPatientInAndStatusOrderByCreatedAtDesc(
                        pacientes, Status.ATIVA, pageable).map(this::toResponse);
            }

            default -> throw new IllegalStateException("Perfil não autorizado");
        };
    }

    @Transactional(readOnly = true)
    public Page<MedicinesResponse> listarMedicamentosInativos(Authentication authentication, Pageable pageable) {
        User user = getUsuarioLogado(authentication);

        return switch (user.getRole()) {
            case PACIENTE -> medicinesRepository
                    .findByPatientIdUserAndStatusOrderByCreatedAtDesc(
                            user.getIdUser(),
                            Status.INATIVA,
                            pageable
                    )
                    .map(this::toResponse);

            case MEDICO -> {
                Professional professional = getProfessionalLogado(authentication);

                List<Patient> pacientes = patientProfessionalRepository
                        .findByProfessionalAndStatus(professional, VinculoStatus.ACEITO)
                        .stream()
                        .map(PatientProfessional::getPatient)
                        .toList();

                yield medicinesRepository.findByPatientInAndStatusOrderByCreatedAtDesc(
                        pacientes, Status.INATIVA, pageable).map(this::toResponse);
            }

            case CUIDADOR -> {
                Caregiver caregiver = getCuidadorLogado(authentication);

                List<Patient> pacientes = caregiverPatientRepository
                        .findByCaregiverAndStatus(caregiver, VinculoStatus.ACEITO)
                        .stream()
                        .map(CaregiverPatient::getPatient)
                        .toList();

                yield medicinesRepository.findByPatientInAndStatusOrderByCreatedAtDesc(
                        pacientes, Status.INATIVA, pageable).map(this::toResponse);
            }

            default -> throw new IllegalStateException("Perfil não autorizado");
        };
    }

    @Transactional(readOnly = true)
    public Page<MedicinesResponse> listarMedicamentosPorPaciente(Authentication authentication, UUID patientId, Pageable pageable) {
        User user = getUsuarioLogado(authentication);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        return switch (user.getRole()) {
            case PACIENTE -> {
                if (!patient.getIdUser().equals(user.getIdUser())) {
                    throw new IllegalStateException("Acesso negado");
                }

                yield medicinesRepository.findByPatientOrderByCreatedAtDesc(patient, pageable).map(this::toResponse);
            }

            case MEDICO -> {
                Professional professional = getProfessionalLogado(authentication);

                if (!patientProfessionalRepository.existsByPatientAndProfessionalAndStatus(
                        patient, professional, VinculoStatus.ACEITO)) {
                    throw new IllegalStateException("Sem vínculo com este paciente");
                }

                yield medicinesRepository
                        .findByPatientOrderByCreatedAtDesc(patient, pageable).map(this::toResponse);
            }

            case CUIDADOR -> {
                Caregiver caregiver = getCuidadorLogado(authentication);

                if (!caregiverPatientRepository.existsByCaregiverAndPatientAndStatus(
                        caregiver, patient, VinculoStatus.ACEITO)) {
                    throw new IllegalStateException("Sem vínculo com este paciente");
                }

                yield medicinesRepository.findByPatientOrderByCreatedAtDesc(patient, pageable).map(this::toResponse);
            }

            default -> throw new IllegalStateException("Perfil não autorizado");
        };
    }


    @Transactional(readOnly = true)
    public MedicinesResponse buscarMedicamento(Authentication authentication, UUID medicineId) {
        User user = getUsuarioLogado(authentication);

        Medicines medicine = medicinesRepository.findById(medicineId)
                .orElseThrow(() -> new EntityNotFoundException("Medicamento não encontrado"));

        Patient patient = medicine.getPatient();

        switch (user.getRole()) {
            case PACIENTE -> {
                if (!patient.getIdUser().equals(user.getIdUser())) {
                    throw new IllegalStateException("Acesso negado ao medicamento");
                }
            }

            case MEDICO -> {
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

        return toResponse(medicine);
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

    private void notificarTodosCuidadores(Patient patient, String titulo, String mensagem, String tipo) {
        List<CaregiverPatient> vinculos = caregiverPatientRepository.findByPatientAndStatus(patient, VinculoStatus.ACEITO);
        for (CaregiverPatient vinculo : vinculos) {
            Caregiver caregiver = vinculo.getCaregiver();
            if (caregiver != null && caregiver.getIdUser() != null) {
                NotificationsRequest notif = new NotificationsRequest(titulo, mensagem, tipo);
                notificationsService.criarNotificacao(caregiver.getIdUser(), notif);
            }
        }
    }

    private MedicinesResponse toResponse(Medicines medicines) {
        return new MedicinesResponse(
                medicines.getIdMedicine(),
                medicines.getName(),
                medicines.getDosage(),
                medicines.getUnity(),
                medicines.getFrequency(),
                medicines.getDescription(),
                medicines.getStatus(),
                medicines.getPatient().getIdUser(),
                medicines.getProfessional().getIdUser()
        );
    }
}

