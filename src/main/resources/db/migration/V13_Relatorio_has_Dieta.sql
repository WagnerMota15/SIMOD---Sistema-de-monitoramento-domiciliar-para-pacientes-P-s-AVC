CREATE EXTENSION IF NOT EXISTS "pgcrypto"

CREATE TABLE dietHasReport(
    diet_id INT,
    report_id INT,
    PRIMARY KEY (diet_id, report_id),
    FOREIGN KEY (diet_id) REFERENCES diet(id) ON DELETE CASCADE,
    FOREIGN KEY (report_id) REFERENCES report(id) ON DELETE CASCADE,
);