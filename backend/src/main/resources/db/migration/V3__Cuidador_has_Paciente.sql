CREATE TABLE caregiver_has_patient(
    caregiver_id UUID NOT NULL,
    patient_id UUID NOT NULL,
    PRIMARY KEY (caregiver_id, patient_id),
    FOREIGN KEY (caregiver_id) REFERENCES caregiver(id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE
);