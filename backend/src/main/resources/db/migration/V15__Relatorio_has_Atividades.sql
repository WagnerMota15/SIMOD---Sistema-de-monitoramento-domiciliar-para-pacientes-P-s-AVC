CREATE TABLE activities_has_report(
    activities_id UUID NOT NULL,
    report_id UUID NOT NULL,
    PRIMARY KEY (activities_id, report_id),
    FOREIGN KEY (activities_id) REFERENCES activities(id) ON DELETE CASCADE,
    FOREIGN KEY (report_id) REFERENCES report(id) ON DELETE CASCADE
);