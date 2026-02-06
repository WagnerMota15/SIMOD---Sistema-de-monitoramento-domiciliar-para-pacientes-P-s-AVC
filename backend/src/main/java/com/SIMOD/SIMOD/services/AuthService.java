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
public class AuthService {

    private final UserRepository userRepository;           // pode manter, mas não usaremos para save
    private final PatientRepository patientRepository;
    private final CaregiverRepository caregiverRepository;
    private final ProfessionalRepository professionalRepository;  // ou repo mais específico se tiver

    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public UUID register(RegisterRequest request) {

        User user = UserFactory.create(request);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        System.out.println("Iniciando registro para role: " + request.role());
        System.out.println("Usuário criado: " + user.getClass().getSimpleName() + " - " + user.getEmail());

        UUID generatedId = switch (request.role()) {
            case PACIENTE -> {
                Patient patient = (Patient) user;
                Patient saved = patientRepository.save(patient);
                System.out.println("Paciente salvo - ID gerado: " + saved.getId());
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