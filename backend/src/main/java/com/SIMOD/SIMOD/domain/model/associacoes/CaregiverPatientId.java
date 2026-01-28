package com.SIMOD.SIMOD.domain.model.associacoes;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CaregiverPatientId implements Serializable {
    private UUID caregiver;
    private UUID patient;
}
