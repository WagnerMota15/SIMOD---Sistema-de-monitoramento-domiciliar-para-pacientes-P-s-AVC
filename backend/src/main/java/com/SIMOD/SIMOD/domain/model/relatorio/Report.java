package com.SIMOD.SIMOD.domain.model.relatorio;

import com.SIMOD.SIMOD.domain.model.atividades.Activities;
import com.SIMOD.SIMOD.domain.model.dieta.Diet;
import com.SIMOD.SIMOD.domain.model.medicamentos.Medicines;
import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import com.SIMOD.SIMOD.domain.model.sessoes.Sessions;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "report")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @GeneratedValue
    private UUID id;
    private LocalDateTime expedience;
    private String assessment;
    private String feedbackPatient;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;

    @ManyToMany(mappedBy = "reports")
    private Set<Activities> activities = new HashSet<>();

    @ManyToMany(mappedBy = "reports")
    private Set<Diet> diets = new HashSet<>();

    @ManyToMany(mappedBy = "reports")
    private Set<Medicines> medicines = new HashSet<>();

    @ManyToMany(mappedBy = "reports")
    private Set<Sessions> sessions = new HashSet<>();
}