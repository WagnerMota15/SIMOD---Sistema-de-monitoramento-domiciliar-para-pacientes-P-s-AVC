package com.SIMOD.SIMOD.domain.cuidador;

import com.SIMOD.SIMOD.domain.paciente.Patient;
import com.SIMOD.SIMOD.domain.relatorio.Report;
import com.SIMOD.SIMOD.domain.usuario.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.util.UUID;

@Entity
@Table(name = "caregiver")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Caregiver extends User {
    @Id
    @GeneratedValue
    private UUID idCuidador;
    private Boolean vinculadoPatient;

    @ManyToMany
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
