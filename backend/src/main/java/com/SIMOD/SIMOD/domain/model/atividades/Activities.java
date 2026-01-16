package com.SIMOD.SIMOD.domain.model.atividades;

import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import com.SIMOD.SIMOD.domain.model.relatorio.Report;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "activities")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Activities {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String description;
    private String typeExercise;
    private int freqRecommended;
    private String videoUrl;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "professional_id")
    private Professional professional;

    @ManyToMany
    @JoinTable(
            name = "activities_has_report",
            joinColumns = @JoinColumn(name = "activities_id"),
            inverseJoinColumns = @JoinColumn(name = "report_id")
    )
    private Set<Report> reports = new HashSet<>();
}