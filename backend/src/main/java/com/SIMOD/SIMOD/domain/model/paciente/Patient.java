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
import com.SIMOD.SIMOD.domain.model.associacoes.CaregiverPatient;
import com.SIMOD.SIMOD.domain.model.associacoes.PatientProfessional;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "patient")
@DiscriminatorValue("PATIENT")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient extends User {

    @Enumerated(EnumType.STRING)
    @Column(name = "stroke_type")
    private StrokeTypes strokeTypes;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CaregiverPatient> caregiverVinculos = new HashSet<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PatientProfessional> professionalVinculos = new HashSet<>();

    @OneToMany(mappedBy = "patient")
    private Set<Activities> activities = new HashSet<>();

    @OneToMany(mappedBy = "patient")
    private Set<Diet> diets = new HashSet<>();

    @OneToMany(mappedBy = "patient")
    private Set<Medicines> medicines = new HashSet<>();

    @OneToMany(mappedBy = "patient")
    private Set<Family> familyMembers = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "patient")
    private Set<Historical> medicalHistory = new HashSet<>();

    public void addFamilyMemmber(Family family){
        familyMembers.add(family);
        family.setPatient(this);
    }

    public UUID getPatientId() {
        return this.getIdUser();
    }
}