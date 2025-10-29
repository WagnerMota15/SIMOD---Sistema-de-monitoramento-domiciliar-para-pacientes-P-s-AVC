package com.SIMOD.SIMOD.domain.paciente;

import com.SIMOD.SIMOD.domain.profissional.Professional;
import com.SIMOD.SIMOD.domain.usuario.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "patient")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue
    private UUID id;
    private String tipoAVC;
    private Boolean vinculoProfessional;
    private Boolean vinculoCaregiver;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinColumn(name = "professional_numCouncil")
    private Professional professional;
}
