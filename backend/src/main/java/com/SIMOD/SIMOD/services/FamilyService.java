package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.domain.model.familiares.Family;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.dto.familia.FamilyRequest;
import com.SIMOD.SIMOD.repositories.FamilyRepository;
import com.SIMOD.SIMOD.repositories.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
    public void createContactFamily(UUID patientId,FamilyRequest request){
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        Family family = new Family(request.name(), request.telephone(), request.kinship());
        patient.addFamilyMemmber(family);

        familyRepository.save(family);

    }

}
