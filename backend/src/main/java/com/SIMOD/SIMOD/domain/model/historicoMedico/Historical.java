package com.SIMOD.SIMOD.domain.model.historicoMedico;

import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "historical")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Historical {
    @Id
    @GeneratedValue
    private UUID idHistorical;
    private String historical;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
