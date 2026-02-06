package com.SIMOD.SIMOD.validation;

import com.SIMOD.SIMOD.domain.enums.Role;
import com.SIMOD.SIMOD.dto.auth.RegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// Implementa uma validação customizada de regras de cadastro,  baseada no tipo de usuário que está sendo registrado.

public class RegistrationRulesValidator
        implements ConstraintValidator<ValidRegistration, RegisterRequest> {

    @Override
    public boolean isValid(RegisterRequest request, ConstraintValidatorContext context) {
        if (request == null) return true;

        Role role = request.role();
        boolean valid = true;

        if (role == Role.PACIENTE) {
            if (request.strokeTypes() == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                "Tipo de AVC (strokeTypes) é obrigatório para pacientes")
                        .addPropertyNode("strokeTypes")
                        .addConstraintViolation();
                valid = false;
            }
        }
        else if (isProfessional(role)) {
            if (request.numCouncil() == null || request.numCouncil().trim().isEmpty()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                "Número do conselho é obrigatório para profissionais da saúde")
                        .addPropertyNode("numCouncil")
                        .addConstraintViolation();
                valid = false;
            }
        }

        return valid;
    }

    private boolean isProfessional(Role role) {
        return role == Role.MEDICO ||
                role == Role.FONOAUDIOLOGO ||
                role == Role.PSICOLOGO ||
                role == Role.NUTRICIONISTA ||
                role == Role.FISIOTERAPEUTA;
    }
}