package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.dto.auth.RegisterPatientRequest;
import com.SIMOD.SIMOD.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    @Autowired
    public PatientRepository patientRepository;

    public Patient criarPaciente(RegisterPatientRequest dado){

        Patient novoPaciente = new Patient();
        novoPaciente.setNameComplete(dado.nomeComplete());
        novoPaciente.setEmail(dado.email());
        novoPaciente.setCPF(dado.CPF());
        novoPaciente.setPassword(dado.password());
        novoPaciente.setTelephone(dado.telephone());
        novoPaciente.setTipoAVC(dado.tipoAVC());
        return patientRepository.save(novoPaciente);

    }
}
