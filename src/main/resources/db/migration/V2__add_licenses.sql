CREATE TABLE licenses (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    license_type VARCHAR(50) NOT NULL,
    description TEXT,
    is_osi_approved BOOLEAN,
    commercial_use_allowed BOOLEAN,
    url VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE INDEX idx_license_name ON licenses(name);
CREATE INDEX idx_license_type ON licenses(license_type);
