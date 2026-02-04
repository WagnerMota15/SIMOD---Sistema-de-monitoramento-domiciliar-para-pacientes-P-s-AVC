CREATE TABLE caregiver_has_patient(
    caregiver_id UUID NOT NULL,
    patient_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    sender VARCHAR(20) NOT NULL,
    request_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    response_date TIMESTAMP WITH TIME ZONE,
    notes TEXT,
    PRIMARY KEY (caregiver_id, patient_id),
    FOREIGN KEY (caregiver_id) REFERENCES caregiver(id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE
);