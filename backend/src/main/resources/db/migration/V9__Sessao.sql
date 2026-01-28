CREATE TABLE sessions(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    date_time TIMESTAMP NOT NULL,
    remote BOOLEAN NOT NULL,
    place VARCHAR(80),
    status VARCHAR(20),
    patient_id UUID NOT NULL,
    professional_id UUID NOT NULL,
    reason_change VARCHAR(500),
    caregiver_id UUID,
    created_by VARCHAR(20),
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
    FOREIGN KEY (professional_id) REFERENCES professional(id) ON DELETE CASCADE,
    FOREIGN KEY (caregiver_id) REFERENCES caregiver(id) ON DELETE CASCADE
);