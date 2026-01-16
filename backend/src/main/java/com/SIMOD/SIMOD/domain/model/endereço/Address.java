package com.SIMOD.SIMOD.domain.model.endereço;

import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
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
    private String number;
    private String description;

    @ManyToMany
    @JoinTable(
            name = "address_has_patient",
            joinColumns = @JoinColumn(name = "address_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id")
    )
    private Set<Patient> patients = new HashSet<>();
}