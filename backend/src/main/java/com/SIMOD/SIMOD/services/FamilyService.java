package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.domain.model.familiares.Family;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.dto.familia.FamilyRequest;
import com.SIMOD.SIMOD.repositories.FamilyRepository;
import com.SIMOD.SIMOD.repositories.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FamilyService {
    private final FamilyRepository familyRepository;
    private final PatientRepository patientRepository;

    public FamilyService(FamilyRepository familyRepository, PatientRepository patientRepository) {
        this.familyRepository = familyRepository;
        this.patientRepository = patientRepository;
    }

    @Transactional
    public void createContacstFamily(UUID patientId, List<FamilyRequest> requests){
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new EntityNotFoundException("PACIENTE NÃO ENCONTRADO"));

        List<Family> contacts = requests.stream()
                .map(request -> toEntity(request, patient))
                .toList();

        familyRepository.saveAll(contacts);
    }

    private Family toEntity(FamilyRequest request, Patient patient) {
        return Family.builder().name(request.name())
                .telephone(request.telephone())
                .kinship(request.kinship())
                .patient(patient)
                .build();
    }

}
