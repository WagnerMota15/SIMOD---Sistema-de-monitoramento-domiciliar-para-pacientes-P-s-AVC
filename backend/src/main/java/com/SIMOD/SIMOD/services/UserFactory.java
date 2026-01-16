package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.domain.model.cuidador.Caregiver;
import com.SIMOD.SIMOD.domain.model.medical.Medico;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
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
        return patient;

    }

    private static Caregiver createCaregiver(RegisterRequest data){
        Caregiver caregiver = new Caregiver();
        fillBase(caregiver,data);
        return caregiver;

    }

    private static Professional createProfessional(RegisterRequest data){
        Professional professional = switch (data.role()){
            case MEDICO -> new Medico();
            //demais cases/instâncias das demais profissões

            default -> throw new IllegalStateException();
        };

        fillBase(professional,data);
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
