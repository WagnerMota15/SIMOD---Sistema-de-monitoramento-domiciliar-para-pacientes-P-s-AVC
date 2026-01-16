package com.SIMOD.SIMOD.domain.model.psicologo;

import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "psychologist")
@DiscriminatorValue("PSYCHOLOGIST")
public class Psychologist extends Professional {
}
