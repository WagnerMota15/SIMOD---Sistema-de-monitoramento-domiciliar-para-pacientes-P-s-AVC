package com.SIMOD.SIMOD.domain.relatorio;

import com.SIMOD.SIMOD.domain.paciente.Patient;
import com.SIMOD.SIMOD.domain.profissional.Professional;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
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
    private Date Expedicao;
    private String avaliacao;
    private String PatientFeedBack;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "professional_numCouncil")
    private Professional professional;
}
