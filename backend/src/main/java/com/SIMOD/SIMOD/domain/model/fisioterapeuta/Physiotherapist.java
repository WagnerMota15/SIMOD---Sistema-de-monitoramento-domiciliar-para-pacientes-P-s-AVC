package com.SIMOD.SIMOD.domain.model.fisioterapeuta;

import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "physiotherapist")
@DiscriminatorValue("PHYSIOTHERAPIST")
public class Physiotherapist extends Professional {

}
