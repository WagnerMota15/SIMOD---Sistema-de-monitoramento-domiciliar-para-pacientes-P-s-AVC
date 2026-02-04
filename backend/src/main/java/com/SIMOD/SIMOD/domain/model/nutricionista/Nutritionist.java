package com.SIMOD.SIMOD.domain.model.nutricionista;

import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "nutritionist")
@DiscriminatorValue("NUTRITIONIST")
public class Nutritionist extends Professional {

}
