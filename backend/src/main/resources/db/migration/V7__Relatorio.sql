CREATE TABLE report(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    expedience TIMESTAMP NOT NULL,
    assessment VARCHAR(200) NOT NULL,
    feedback_patient VARCHAR(45),
    patient_id UUID NOT NULL,
    professional_id UUID NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
    FOREIGN KEY (professional_id) REFERENCES professional(id) ON DELETE CASCADE
);