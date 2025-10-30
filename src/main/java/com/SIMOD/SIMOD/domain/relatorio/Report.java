package com.SIMOD.SIMOD.domain.relatorio;

import com.SIMOD.SIMOD.domain.atividades.Activities;
import com.SIMOD.SIMOD.domain.dieta.Diet;
import com.SIMOD.SIMOD.domain.medicamentos.Medicines;
import com.SIMOD.SIMOD.domain.paciente.Patient;
import com.SIMOD.SIMOD.domain.profissional.Professional;
import com.SIMOD.SIMOD.domain.sessoes.Sessions;
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
    @Column(name = "data_expedicao", nullable = false)
    private LocalDateTime dataExpedicao;
    private String avaliacao;
    @Column(name = "patient_feedback")
    private String patientFeedback;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "professional_num_council", nullable = false)
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