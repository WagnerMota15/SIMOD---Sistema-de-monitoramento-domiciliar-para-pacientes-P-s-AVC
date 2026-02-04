CREATE TABLE health_diary_medicine(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    diary_id UUID NOT NULL,
    medicine_id UUID NOT NULL,
    taken BOOLEAN DEFAULT FALSE,
    dose_taken DECIMAL(5,2),
    unity_taken VARCHAR(10),
    time_taken TIME,
    note TEXT,
    CONSTRAINT fk_hdm_diary FOREIGN KEY (diary_id) REFERENCES health_diary(id) ON DELETE CASCADE,
    CONSTRAINT fk_hdm_medicine FOREIGN KEY (medicine_id) REFERENCES medicines(id_medicine) ON DELETE CASCADE,
    CONSTRAINT unique_diary_medicine UNIQUE (diary_id, medicine_id)
);
