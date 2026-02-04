CREATE TABLE licenses (
    id bigserial PRIMARY KEY,
    name varchar(100) NOT NULL UNIQUE,
    license_type varchar(50) NOT NULL,
    description text,
    is_osi_approved boolean,
    commercial_use_allowed boolean,
    url varchar(500),
    created_at timestamp NOT NULL,
    updated_at timestamp
);

CREATE UNIQUE INDEX idx_license_name ON licenses(name);
CREATE INDEX idx_license_type ON licenses(license_type);
