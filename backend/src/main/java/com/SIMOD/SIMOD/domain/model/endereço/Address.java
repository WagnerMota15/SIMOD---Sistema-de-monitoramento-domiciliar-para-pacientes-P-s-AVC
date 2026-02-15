package com.SIMOD.SIMOD.domain.model.endereço;

import com.SIMOD.SIMOD.domain.model.paciente.Patient;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "address")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @Id
    @GeneratedValue
    private UUID id;

    private String cep;
    private String street;
    private String neighborhood;
    private String city;
    private String state;
    private String number;

    @OneToMany(mappedBy = "address")
    private Set<Patient> patients = new HashSet<>();

    public Address(String cep, String street, String neighborhood, String city, String state, String number) {
        this.cep = cep;
        this.street = street;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.number = number;
    }
}