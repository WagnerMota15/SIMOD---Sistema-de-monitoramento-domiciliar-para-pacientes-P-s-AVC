package com.SIMOD.SIMOD.domain.historicoMedico;

import com.SIMOD.SIMOD.domain.paciente.Patient;
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
    private UUID id;
    private String historico;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
