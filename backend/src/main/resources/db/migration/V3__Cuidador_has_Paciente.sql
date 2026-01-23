CREATE TABLE caregiver_has_patient(
    caregiver_id UUID NOT NULL,
    patient_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    remetente VARCHAR(20) NOT NULL,
    data_solicitacao TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_resposta TIMESTAMP WITH TIME ZONE,
    observacao TEXT,
    PRIMARY KEY (caregiver_id, patient_id),
    FOREIGN KEY (caregiver_id) REFERENCES caregiver(id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE
);