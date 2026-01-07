package com.SIMOD.SIMOD.domain.profissional;

import com.SIMOD.SIMOD.domain.paciente.Patient;
import com.SIMOD.SIMOD.domain.usuario.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "professional")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Professional extends User{

    private String numCouncil;

    @ManyToMany
    @JoinTable(
            name = "patient_professional",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id")
    )
    private Set<Patient> patients = new HashSet<>();
}