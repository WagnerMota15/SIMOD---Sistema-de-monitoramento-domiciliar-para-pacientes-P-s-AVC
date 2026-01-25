CREATE TABLE reminders(
    id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    type VARCHAR(20) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    scheduled_at TIMESTAMP NOT NULL,
    recurring BOOLEAN DEFAULT FALSE,
    interval_type VARCHAR(20),
    confirmed BOOLEAN DEFAULT FALSE,
    confirmed_at TIMESTAMP,
    created_by VARCHAR(20) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reminder_patient
        FOREIGN KEY (patient_id)
            REFERENCES patient(id)
            ON DELETE CASCADE
);