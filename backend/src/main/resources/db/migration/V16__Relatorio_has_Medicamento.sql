CREATE TABLE medicines_has_report(
    medicine_id UUID NOT NULL,
    report_id UUID NOT NULL,
    PRIMARY KEY (medicine_id, report_id),
    FOREIGN KEY (medicine_id) REFERENCES medicines(id_medicine) ON DELETE CASCADE,
    FOREIGN KEY (report_id) REFERENCES report(id) ON DELETE CASCADE
);