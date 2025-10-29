package com.SIMOD.SIMOD.domain.dieta;

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
@Table(name = "Diet")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Diet {
    @Id
    @GeneratedValue
    private UUID id;
    private int frenqRefeicoes;
    private String horarios;
    private String descricao;

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
