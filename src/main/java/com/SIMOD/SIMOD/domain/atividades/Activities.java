package com.SIMOD.SIMOD.domain.atividades;

import com.SIMOD.SIMOD.domain.paciente.Patient;
import com.SIMOD.SIMOD.domain.profissional.Professional;
import com.SIMOD.SIMOD.domain.relatorio.Report;
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
    private UUID idAtividade;

    private String nome;
    private String descricao;
    private String tipoExercicio;
    private int freqRecomendada;
    private String videoUrl;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "professional_numCouncil")
    private Professional professional;

    // CORRIGIDO: ManyToMany com JoinTable + coleção de Report
    @ManyToMany
    @JoinTable(
            name = "activity_report",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "report_id")
    )
    private Set<Report> reports = new HashSet<>();
}