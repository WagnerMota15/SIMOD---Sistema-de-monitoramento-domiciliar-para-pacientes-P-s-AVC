CREATE TABLE health_diary_session (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    diary_id UUID NOT NULL,
    session_id UUID NOT NULL,
    attended BOOLEAN DEFAULT FALSE,
    note TEXT,
    CONSTRAINT fk_hds_diary FOREIGN KEY (diary_id) REFERENCES health_diary(id) ON DELETE CASCADE,
    CONSTRAINT fk_hds_session FOREIGN KEY (session_id) REFERENCES sessions(id) ON DELETE CASCADE,
    CONSTRAINT unique_diary_session UNIQUE (diary_id, session_id)
);

