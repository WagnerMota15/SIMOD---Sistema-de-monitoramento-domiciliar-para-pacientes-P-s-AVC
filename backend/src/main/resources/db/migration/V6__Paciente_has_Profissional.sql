CREATE TABLE professional_has_patient (
    professional_id UUID NOT NULL,
    patient_id UUID NOT NULL,
    PRIMARY KEY (professional_id, patient_id),

    CONSTRAINT fk_ph_professional
        FOREIGN KEY (professional_id)
        REFERENCES professional(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_ph_patient
        FOREIGN KEY (patient_id)
        REFERENCES patient(id)
        ON DELETE CASCADE
);
