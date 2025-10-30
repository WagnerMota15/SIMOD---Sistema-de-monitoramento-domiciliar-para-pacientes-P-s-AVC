CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE sessionsHasReport(
    sessions_id UUID NOT NULL,
    report_id UUID NOT NULL,
    PRIMARY KEY (session_id, report_id),
    FOREIGN KEY (sessions_id) REFERENCES sessions(id) ON DELETE CASCADE,
    FOREIGN KEY (report_id) REFERENCES report(id) ON DELETE CASCADE,
);