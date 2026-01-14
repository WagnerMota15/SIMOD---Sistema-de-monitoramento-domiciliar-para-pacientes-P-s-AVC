package com.SIMOD.SIMOD.domain.model.cuidador;

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
@Table(name = "caregiver")
@DiscriminatorValue("CAREGIVER")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Caregiver extends User{

    @ManyToMany
    @JoinTable(
            name = "caregiver_patient",
            joinColumns = @JoinColumn(name = "caregiver_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id")
    )
    private Set<Patient> patients = new HashSet<>();
}