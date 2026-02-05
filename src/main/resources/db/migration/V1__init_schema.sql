CREATE TABLE libraries (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    version VARCHAR(50) NOT NULL,
    source VARCHAR(50) NOT NULL,
    license_name VARCHAR(100),
    health_score INTEGER,
    last_release VARCHAR(50),
    repository VARCHAR(500),
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE INDEX idx_library_name ON libraries(name);
CREATE INDEX idx_library_source ON libraries(source);
CREATE INDEX idx_library_health_score ON libraries(health_score);
