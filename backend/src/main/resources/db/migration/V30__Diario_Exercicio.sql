CREATE TABLE health_diary_activity (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    diary_id UUID NOT NULL,
    activity_id UUID NOT NULL,
    completed BOOLEAN DEFAULT FALSE,
    time_completed TIME,
    note TEXT,
    CONSTRAINT fk_hda_diary FOREIGN KEY (diary_id) REFERENCES health_diary(id) ON DELETE CASCADE,
    CONSTRAINT fk_hda_activity FOREIGN KEY (activity_id) REFERENCES activities(id) ON DELETE CASCADE
);
