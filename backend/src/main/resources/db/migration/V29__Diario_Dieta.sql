CREATE TABLE health_diary_diet (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    diary_id UUID NOT NULL,
    diet_id UUID NOT NULL,
    followed BOOLEAN DEFAULT FALSE,
    note TEXT,
    CONSTRAINT fk_hdd_diary FOREIGN KEY (diary_id) REFERENCES health_diary(id) ON DELETE CASCADE,
    CONSTRAINT fk_hdd_diet FOREIGN KEY (diet_id) REFERENCES diet(id_diet) ON DELETE CASCADE,
    CONSTRAINT unique_diary_diet UNIQUE (diary_id, diet_id)
);

