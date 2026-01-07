CREATE TABLE professionalHasPatient(
     professional_id UUID NOT NULL,
     patient_id UUID NOT NULL,
     PRIMARY KEY (professional_id, patient_id),
     FOREIGN KEY (professional_id) REFERENCES professional_id ON DELETE CASCADE,
     FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE
);