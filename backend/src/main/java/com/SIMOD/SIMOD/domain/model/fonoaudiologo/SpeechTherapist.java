package com.SIMOD.SIMOD.domain.model.fonoaudiologo;

import com.SIMOD.SIMOD.domain.model.profissional.Professional;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "speech_therapist")
@DiscriminatorValue("SPEECH_THERAPIST")
public class SpeechTherapist extends Professional {

}
