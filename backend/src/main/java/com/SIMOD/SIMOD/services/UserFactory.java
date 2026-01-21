package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.domain.model.cuidador.Caregiver;
import com.SIMOD.SIMOD.domain.model.fisioterapeuta.Physiotherapist;
import com.SIMOD.SIMOD.domain.model.fonoaudiologo.SpeechTherapist;
import com.SIMOD.SIMOD.domain.model.medico.Medical;
import com.SIMOD.SIMOD.domain.model.nutricionista.Nutritionist;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import com.SIMOD.SIMOD.domain.model.psicologo.Psychologist;
import com.SIMOD.SIMOD.domain.model.usuario.User;
import com.SIMOD.SIMOD.dto.auth.RegisterRequest;


public class UserFactory {

    public static User create(RegisterRequest request){

        return switch(request.role()){

            case PACIENTE -> createPaciente(request);
            case CUIDADOR -> createCaregiver(request);
            case MEDICO,FONOAUDIOLOGO,PSICOLOGO,NUTRICIONISTA,FISIOTERAPEUTA -> createProfessional(request);

        };

    }


    private static Patient createPaciente(RegisterRequest data){
        Patient patient = new Patient();
        fillBase(patient,data);
        patient.setStrokeTypes(data.strokeTypes());
        return patient;

    }

    private static Caregiver createCaregiver(RegisterRequest data){
        Caregiver caregiver = new Caregiver();
        fillBase(caregiver,data);
        return caregiver;

    }

    private static Professional createProfessional(RegisterRequest data){
        Professional professional = switch (data.role()){
            case MEDICO -> new Medical();
            case PSICOLOGO -> new Psychologist();
            case FISIOTERAPEUTA -> new Physiotherapist();
            case NUTRICIONISTA -> new Nutritionist();
            case FONOAUDIOLOGO -> new SpeechTherapist();

            default -> throw new IllegalStateException();
        };

        professional.setNameComplete(data.nameComplete());
        professional.setCpf(data.cpf());
        professional.setEmail(data.email());
        professional.setPassword(data.password());
        professional.setTelephone(data.telephone());
        professional.setRole(data.role());
        professional.setNumCouncil(data.numCouncil());

        return professional;
    }


    //função para evitar repetições de atrbuições de requisições
    public static void fillBase(User user,RegisterRequest request){

        user.setNameComplete(request.nameComplete());
        user.setCpf(request.cpf());
        user.setTelephone(request.telephone());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setRole(request.role());

    }
}
