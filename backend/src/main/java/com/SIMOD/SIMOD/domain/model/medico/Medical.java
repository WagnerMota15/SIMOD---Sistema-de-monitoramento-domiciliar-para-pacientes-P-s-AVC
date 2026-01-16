package com.SIMOD.SIMOD.domain.model.medico;

import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "medical")
@DiscriminatorValue("MEDICAL")
public class Medical extends Professional {

}
