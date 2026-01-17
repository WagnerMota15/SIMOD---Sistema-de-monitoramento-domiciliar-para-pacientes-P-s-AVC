package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.domain.model.cuidador.Caregiver;
import com.SIMOD.SIMOD.dto.caregiver.CaregiverRequest;
import com.SIMOD.SIMOD.repositories.CaregiverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaregiverService {
    @Autowired
    public CaregiverRepository caregiverRepository;

    public Caregiver criarCuidador(CaregiverRequest dado){

        Caregiver novoCuidador = new Caregiver();
        novoCuidador.setCpf(dado.CPF());
        novoCuidador.setNameComplete(dado.nomeComplete());
        novoCuidador.setEmail(dado.email());
        novoCuidador.setPassword(dado.password());
        novoCuidador.setTelephone(dado.telephone());

        return caregiverRepository.save(novoCuidador);
    }
}
