CREATE EXTENSION IF NOT EXISTS "pgcrypto"

CREATE TABLE medicineHasReport(
    medicine_id INT,
    report_id INT,
    PRIMARY KEY (medicine_id, report_id),
    FOREIGN KEY (medicine_id) REFERENCES medicine(id) ON DELETE CASCADE,
    FOREIGN KEY (report_id) REFERENCES report(id) ON DELETE CASCADE,
);