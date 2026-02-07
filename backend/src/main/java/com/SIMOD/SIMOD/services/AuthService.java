package com.SIMOD.SIMOD.services;

import com.SIMOD.SIMOD.domain.model.cuidador.Caregiver;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import com.SIMOD.SIMOD.domain.model.usuario.User;
import com.SIMOD.SIMOD.dto.auth.RegisterRequest;
import com.SIMOD.SIMOD.repositories.CaregiverRepository;
import com.SIMOD.SIMOD.repositories.PatientRepository;
import com.SIMOD.SIMOD.repositories.ProfessionalRepository;
import com.SIMOD.SIMOD.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor

// Camada de serviço responsável pelos casos de uso de autenticação e registro.
// Contém lógica de aplicação e orquestra repositórios, mantendo entidades desacopladas da infraestrutura.
public class AuthService {

    private final PatientRepository patientRepository;
    private final CaregiverRepository caregiverRepository;
    private final ProfessionalRepository professionalRepository;  // ou repo mais específico se tiver

    // Componente responsável por hash seguro de senhas (BCrypt)
    // Evita armazenamento de senha em texto plano (requisito de segurança)
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public UUID register(RegisterRequest request) {

        // Cria a entidade correta (Patient, Caregiver ou Professional)
        // com base no papel informado no request
        // Factory centraliza a regra de criação e evita lógica condicional espalhada
        User user = UserFactory.create(request);

        // Aplica hash na senha antes de persistir no bd
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        System.out.println("Iniciando registro para role: " + request.role());
        System.out.println("Usuário criado: " + user.getClass().getSimpleName() + " - " + user.getEmail());

        // Seleciona o fluxo de persistência de acordo com o papel do usuário(role) e utilizando o switch
        //evitando extensos if/else
        UUID generatedId = switch (request.role()) {
            case PACIENTE -> {
                Patient patient = (Patient) user;

                //aqui o usuário então é persistido no banco,chamando o método .save
                //gerada em execução pelo JPA repo(patientRepository),que gera a interface concreta e contendo esse método
                Patient saved = patientRepository.save(patient);
                System.out.println("Paciente salvo - ID gerado: " + saved.getId());

                //o yield foi utilizado para retornar o valor do case(UUID),melhor decisão para esse trecho de código
                yield saved.getId();
            }
            case CUIDADOR -> {
                Caregiver caregiver = (Caregiver) user;
                Caregiver saved = caregiverRepository.save(caregiver);
                System.out.println("Cuidador salvo - ID gerado: " + saved.getId());
                yield saved.getId();
            }
            case MEDICO, FONOAUDIOLOGO, PSICOLOGO, NUTRICIONISTA, FISIOTERAPEUTA -> {
                Professional professional = (Professional) user;
                Professional saved = professionalRepository.save(professional);
                System.out.println("Profissional salvo - ID gerado: " + saved.getId());
                yield saved.getId();
            }
            default -> throw new IllegalArgumentException("Role não suportado: " + request.role());
        };

        System.out.println("Registro concluído - ID retornado: " + generatedId);
        return generatedId;
    }
}