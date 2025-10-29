package com.SIMOD.SIMOD.domain.atividades;

import com.SIMOD.SIMOD.domain.paciente.Patient;
import com.SIMOD.SIMOD.domain.profissional.Professional;
import com.SIMOD.SIMOD.domain.relatorio.Report;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToMany
    @JoinColumn(name = "report_id")
    private Report report;
}
