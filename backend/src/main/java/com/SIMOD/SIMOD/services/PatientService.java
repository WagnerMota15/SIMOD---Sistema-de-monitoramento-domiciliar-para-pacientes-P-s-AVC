package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.dto.patient.PatientRequest;
import com.SIMOD.SIMOD.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    @Autowired
    public PatientRepository patientRepository;

    public Patient criarPaciente(PatientRequest dado){

        Patient novoPaciente = new Patient();
        novoPaciente.setNameComplete(dado.nomeComplete());
        novoPaciente.setEmail(dado.email());
        novoPaciente.setCpf(dado.CPF());
        novoPaciente.setPassword(dado.password());
        novoPaciente.setTelephone(dado.telephone());
//        novoPaciente.setTipoAVC(dado.tipoAVC());
        return patientRepository.save(novoPaciente);

    }
}
