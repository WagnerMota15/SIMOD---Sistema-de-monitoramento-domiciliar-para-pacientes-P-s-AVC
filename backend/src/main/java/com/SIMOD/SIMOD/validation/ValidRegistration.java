package com.SIMOD.SIMOD.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RegistrationRulesValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRegistration {

    String message() default "Regras de cadastro inválidas para o tipo de usuário";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}