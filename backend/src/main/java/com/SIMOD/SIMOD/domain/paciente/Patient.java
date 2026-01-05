package com.SIMOD.SIMOD.domain.paciente;

import com.SIMOD.SIMOD.domain.atividades.Activities;
import com.SIMOD.SIMOD.domain.cuidador.Caregiver;
import com.SIMOD.SIMOD.domain.dieta.Diet;
import com.SIMOD.SIMOD.domain.endereço.Address;
import com.SIMOD.SIMOD.domain.familiares.Family;
import com.SIMOD.SIMOD.domain.historicoMedico.Historical;
import com.SIMOD.SIMOD.domain.medicamentos.Medicines;
import com.SIMOD.SIMOD.domain.profissional.Professional;
import com.SIMOD.SIMOD.domain.usuario.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "patient")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Patient extends User{
    
    private String tipoAVC;

    @ManyToMany
    @JoinTable(
            name = "patient_professional",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "professional_id")
    )
    private Set<Professional> professionals = new HashSet<>();

    @OneToMany(mappedBy = "patient")
    private Set<Activities> activities = new HashSet<>();

    @OneToMany(mappedBy = "patient")
    private Set<Diet> diets = new HashSet<>();

    @OneToMany(mappedBy = "patient")
    private Set<Medicines> medicines = new HashSet<>();

    @OneToMany(mappedBy = "patient")
    private Set<Family> familyMembers = new HashSet<>();

    @OneToMany(mappedBy = "patient")
    private Set<Historical> medicalHistory = new HashSet<>();

    @ManyToMany(mappedBy = "patients")
    private Set<Address> addresses = new HashSet<>();

    @ManyToMany(mappedBy = "patients")
    private Set<Caregiver> caregivers = new HashSet<>();
}
