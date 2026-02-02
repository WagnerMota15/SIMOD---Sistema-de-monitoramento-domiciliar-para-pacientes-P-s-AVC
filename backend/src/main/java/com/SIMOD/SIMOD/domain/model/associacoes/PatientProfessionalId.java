package com.SIMOD.SIMOD.domain.model.associacoes;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PatientProfessionalId implements Serializable {
    private UUID patient;
    private UUID professional;
}
