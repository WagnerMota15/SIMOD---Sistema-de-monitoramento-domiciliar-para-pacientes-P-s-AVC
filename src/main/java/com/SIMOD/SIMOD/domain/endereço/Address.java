package com.SIMOD.SIMOD.domain.endereço;

import com.SIMOD.SIMOD.domain.paciente.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "address")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue
    private UUID id;
    private String cep;
    private String numero;
    private String descricao;

    @ManyToMany
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
