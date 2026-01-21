package com.SIMOD.SIMOD.domain.model.familiares;

import com.SIMOD.SIMOD.domain.enums.Kinship;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "family")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Family {
    @Id
    @GeneratedValue
    private UUID idFamily;
    private String name;
    private String telephone;

    @Enumerated(EnumType.STRING)
    private Kinship kinship;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}