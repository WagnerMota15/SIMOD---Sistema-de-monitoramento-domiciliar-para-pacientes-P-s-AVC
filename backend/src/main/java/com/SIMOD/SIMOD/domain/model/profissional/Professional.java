package com.SIMOD.SIMOD.domain.model.profissional;

import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.usuario.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "professional")
@PrimaryKeyJoinColumn(name = "id")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Professional extends User{
    @Column(name = "num_council")
    private String numCouncil;

    @ManyToMany
    @JoinTable(
            name = "professional_has_patient",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id")
    )
    private Set<Patient> patients = new HashSet<>();
}