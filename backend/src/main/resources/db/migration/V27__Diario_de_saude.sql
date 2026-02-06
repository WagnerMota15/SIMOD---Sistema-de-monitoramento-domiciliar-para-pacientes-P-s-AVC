create table health_diary(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    patient_id UUID NOT NULL,
    caregiver_id UUID,
    patient_name VARCHAR(100) NOT NULL,
    diary_date DATE NOT NULL,
    systolic_bp INT,
    diastolic_bp INT,
    heart_rate INT,
    weight DECIMAL(5,2),
    glucose DECIMAL(6,2),
    symptoms TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_hd_patient FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE,
    CONSTRAINT fk_hd_caregiver FOREIGN KEY (caregiver_id) REFERENCES caregiver(id) ON DELETE SET NULL,
    CONSTRAINT unique_patient_diary_date UNIQUE (patient_id, diary_date)
);

