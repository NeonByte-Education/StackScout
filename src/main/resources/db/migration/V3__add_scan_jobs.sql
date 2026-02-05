CREATE TABLE scan_jobs (
    id BIGSERIAL PRIMARY KEY,
    source VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    packages_count INTEGER,
    processed_count INTEGER,
    failed_count INTEGER,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    error_message TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE INDEX idx_scan_job_status ON scan_jobs(status);
CREATE INDEX idx_scan_job_source ON scan_jobs(source);
CREATE INDEX idx_scan_job_created ON scan_jobs(created_at);
