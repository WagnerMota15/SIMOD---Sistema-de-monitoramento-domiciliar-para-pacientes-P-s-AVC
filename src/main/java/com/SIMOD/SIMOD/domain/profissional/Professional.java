package com.SIMOD.SIMOD.domain.profissional;

import com.SIMOD.SIMOD.domain.paciente.Patient;
import com.SIMOD.SIMOD.domain.usuario.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "professional")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Professional {
    private String numCouncil;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
