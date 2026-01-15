package com.SIMOD.SIMOD.domain.model.paciente;

import com.SIMOD.SIMOD.domain.enums.StrokeTypes;
import com.SIMOD.SIMOD.domain.model.atividades.Activities;
import com.SIMOD.SIMOD.domain.model.cuidador.Caregiver;
import com.SIMOD.SIMOD.domain.model.dieta.Diet;
import com.SIMOD.SIMOD.domain.model.endereço.Address;
import com.SIMOD.SIMOD.domain.model.familiares.Family;
import com.SIMOD.SIMOD.domain.model.historicoMedico.Historical;
import com.SIMOD.SIMOD.domain.model.medicamentos.Medicines;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import com.SIMOD.SIMOD.domain.model.usuario.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "patient")
@DiscriminatorValue("PATIENT")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Patient extends User{

    @Enumerated(EnumType.STRING)
    @Column(name = "stroke_type")
    private StrokeTypes strokeTypes;

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
