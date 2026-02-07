-- StackScout PostgreSQL Initialization Script
-- Создание базовой схемы БД и таблиц

-- Включить необходимые расширения
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- Создать основные таблицы
CREATE TABLE IF NOT EXISTS packages (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    version VARCHAR(100) NOT NULL,
    description TEXT,
    source_type VARCHAR(50) NOT NULL,
    repository_url VARCHAR(500),
    homepage_url VARCHAR(500),
    health_score INTEGER CHECK (health_score >= 0 AND health_score <= 100),
    actuality_score INTEGER,
    activity_score INTEGER,
    repository_score INTEGER,
    community_score INTEGER,
    license VARCHAR(100),
    downloads_count BIGINT DEFAULT 0,
    author VARCHAR(255),
    maintainer VARCHAR(255),
    last_update TIMESTAMP,
    last_checked TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS licenses (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    classification VARCHAR(50) NOT NULL,
    url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS projects (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    source_url VARCHAR(500),
    status VARCHAR(50) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS project_dependencies (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    package_id BIGINT NOT NULL REFERENCES packages(id) ON DELETE RESTRICT,
    version_specified VARCHAR(100) NOT NULL,
    version_locked VARCHAR(100),
    status VARCHAR(50) DEFAULT 'ACTIVE',
    license_compliant BOOLEAN DEFAULT TRUE,
    has_vulnerabilities BOOLEAN DEFAULT FALSE,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    checked_at TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS package_dependencies (
    id BIGSERIAL PRIMARY KEY,
    package_id BIGINT NOT NULL REFERENCES packages(id) ON DELETE CASCADE,
    dependency_id BIGINT NOT NULL REFERENCES packages(id) ON DELETE RESTRICT,
    version_range VARCHAR(100),
    is_optional BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS vulnerabilities (
    id BIGSERIAL PRIMARY KEY,
    package_id BIGINT NOT NULL REFERENCES packages(id) ON DELETE CASCADE,
    cve_id VARCHAR(50) UNIQUE,
    severity VARCHAR(50),
    description TEXT,
    affected_versions_from VARCHAR(100),
    affected_versions_to VARCHAR(100),
    patched_versions VARCHAR(500),
    source_url VARCHAR(500),
    published_at TIMESTAMP,
    discovered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS health_check_logs (
    id BIGSERIAL PRIMARY KEY,
    package_id BIGINT NOT NULL REFERENCES packages(id) ON DELETE CASCADE,
    health_score INTEGER,
    actuality_score INTEGER,
    activity_score INTEGER,
    repository_score INTEGER,
    community_score INTEGER,
    source VARCHAR(100),
    status VARCHAR(50),
    error_message TEXT,
    checked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создать индексы для оптимизации запросов
CREATE INDEX IF NOT EXISTS idx_packages_name ON packages(name);
CREATE INDEX IF NOT EXISTS idx_packages_health_score ON packages(health_score DESC);
CREATE INDEX IF NOT EXISTS idx_packages_source_type ON packages(source_type);
CREATE INDEX IF NOT EXISTS idx_packages_last_update ON packages(last_update DESC);
CREATE INDEX IF NOT EXISTS idx_licenses_classification ON licenses(classification);
CREATE INDEX IF NOT EXISTS idx_project_deps_project_id ON project_dependencies(project_id);
CREATE INDEX IF NOT EXISTS idx_project_deps_package_id ON project_dependencies(package_id);
CREATE INDEX IF NOT EXISTS idx_package_deps_package_id ON package_dependencies(package_id);
CREATE INDEX IF NOT EXISTS idx_vulnerabilities_severity ON vulnerabilities(severity);
CREATE INDEX IF NOT EXISTS idx_vulnerabilities_package_id ON vulnerabilities(package_id);
CREATE INDEX IF NOT EXISTS idx_health_check_package_id ON health_check_logs(package_id);

-- Вставить стандартные лицензии
INSERT INTO licenses (name, description, classification) VALUES
('MIT', 'MIT License', 'PERMISSIVE'),
('Apache-2.0', 'Apache License 2.0', 'PERMISSIVE'),
('GPL-3.0', 'GNU General Public License v3', 'COPYLEFT'),
('GPL-2.0', 'GNU General Public License v2', 'COPYLEFT'),
('AGPL-3.0', 'GNU Affero General Public License v3', 'COPYLEFT'),
('BSD-3-Clause', 'BSD 3-Clause License', 'PERMISSIVE'),
('BSD-2-Clause', 'BSD 2-Clause License', 'PERMISSIVE'),
('ISC', 'ISC License', 'PERMISSIVE'),
('MPL-2.0', 'Mozilla Public License 2.0', 'COPYLEFT'),
('LGPL-3.0', 'GNU Lesser General Public License v3', 'COPYLEFT'),
('Unlicense', 'The Unlicense', 'PERMISSIVE'),
('CC0-1.0', 'Creative Commons Zero v1.0', 'PERMISSIVE')
ON CONFLICT (name) DO NOTHING;

-- Создать функцию для обновления updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Применить триггеры
CREATE TRIGGER update_packages_updated_at BEFORE UPDATE ON packages
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_projects_updated_at BEFORE UPDATE ON projects
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_project_dependencies_updated_at BEFORE UPDATE ON project_dependencies
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_package_dependencies_updated_at BEFORE UPDATE ON package_dependencies
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Коммент для отслеживания версии
COMMENT ON SCHEMA public IS 'StackScout Database Schema v1.0';
