CREATE EXTENSION IF NOT EXISTS "pgcrypto"

CREATE TABLE activitiesHasReport(
    activities_id INT,
    report_id INT,
    PRIMARY KEY (activities_id, report_id),
    FOREIGN KEY (activities_id) REFERENCES activities(id) ON DELETE CASCADE,
    FOREIGN KEY (report_id) REFERENCES report(id) ON DELETE CASCADE,
);