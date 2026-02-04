CREATE TABLE scan_jobs (
    id bigserial PRIMARY KEY,
    source varchar(50) NOT NULL,
    status varchar(50) NOT NULL,
    packages_count integer,
    processed_count integer,
    failed_count integer,
    started_at timestamp,
    completed_at timestamp,
    error_message text,
    created_at timestamp NOT NULL,
    updated_at timestamp
);

CREATE INDEX idx_scan_job_status ON scan_jobs(status);
CREATE INDEX idx_scan_job_source ON scan_jobs(source);
CREATE INDEX idx_scan_job_created ON scan_jobs(created_at);
