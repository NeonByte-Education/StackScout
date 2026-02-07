# Документация базы данных

<div align="center">

**Структура данных и схема PostgreSQL для StackScout**

[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?logo=postgresql)](https://www.postgresql.org/)

[Обзор](#обзор) • [Сущности](#основные-сущности) • [Миграции](#миграции) • [Обслуживание](#обслуживание)

</div>

---

## Обзор

### Информация о БД

- **Тип**: PostgreSQL 16
- **Название**: `stackscout`
- **Пользователь**: `postgres`
- **Портал**: `5432`
- **Миграции**: Flyway

### Инициализация БД

```bash
# Автоматически при запуске через Spring Boot
# Flyway проверит миграции и применит необходимые

# Или вручную через Docker
docker-compose exec postgres psql -U postgres -d stackscout
```

---

## Основные сущности

### 1. Package (Пакет)

Основная таблица для хранения информации о библиотеках.

```sql
CREATE TABLE packages (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    version VARCHAR(100) NOT NULL,
    description TEXT,
    source_type VARCHAR(50) NOT NULL,  -- PYPI, DOCKER_HUB
    repository_url VARCHAR(500),
    homepage_url VARCHAR(500),
    
    -- Метрики здоровья
    health_score INTEGER CHECK (health_score >= 0 AND health_score <= 100),
    actuality_score INTEGER,
    activity_score INTEGER,
    repository_score INTEGER,
    community_score INTEGER,
    
    -- Информация о пакете
    license VARCHAR(100),
    downloads_count BIGINT DEFAULT 0,
    author VARCHAR(255),
    maintainer VARCHAR(255),
    
    -- Временные метки
    last_update TIMESTAMP,
    last_checked TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_packages_name ON packages(name);
CREATE INDEX idx_packages_source_type ON packages(source_type);
CREATE INDEX idx_packages_health_score ON packages(health_score DESC);
CREATE INDEX idx_packages_last_update ON packages(last_update DESC);
```

### 2. License (Лицензия)

Справочная таблица лицензий с классификацией.

```sql
CREATE TABLE licenses (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,  -- SPDX identifier
    description TEXT,
    classification VARCHAR(50) NOT NULL,  -- PERMISSIVE, COPYLEFT, PROPRIETARY
    url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO licenses (name, description, classification) VALUES
('MIT', 'MIT License', 'PERMISSIVE'),
('Apache-2.0', 'Apache License 2.0', 'PERMISSIVE'),
('GPL-3.0', 'GNU General Public License v3', 'COPYLEFT'),
('BSD-3-Clause', 'BSD 3-Clause License', 'PERMISSIVE');

CREATE INDEX idx_licenses_name ON licenses(name);
CREATE INDEX idx_licenses_classification ON licenses(classification);
```

### 3. License Compatibility (Совместимость лицензий)

Матрица совместимости между лицензиями.

```sql
CREATE TABLE license_compatibility (
    id BIGSERIAL PRIMARY KEY,
    license_id_1 BIGINT NOT NULL REFERENCES licenses(id) ON DELETE CASCADE,
    license_id_2 BIGINT NOT NULL REFERENCES licenses(id) ON DELETE CASCADE,
    is_compatible BOOLEAN NOT NULL DEFAULT TRUE,
    notes VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(license_id_1, license_id_2)
);

CREATE INDEX idx_license_compat_license_1 ON license_compatibility(license_id_1);
CREATE INDEX idx_license_compat_license_2 ON license_compatibility(license_id_2);
```

### 4. Project (Проект)

Пользовательские проекты с зависимостями.

```sql
CREATE TABLE projects (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    source_url VARCHAR(500),
    
    -- Статус проекта
    status VARCHAR(50) DEFAULT 'ACTIVE',  -- ACTIVE, ARCHIVED, ON_HOLD
    
    -- Временные метки
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_projects_name ON projects(name);
CREATE INDEX idx_projects_status ON projects(status);
CREATE INDEX idx_projects_created_at ON projects(created_at DESC);
```

### 5. Project Dependency (Зависимости проекта)

Связь между проектами и пакетам.

```sql
CREATE TABLE project_dependencies (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    package_id BIGINT NOT NULL REFERENCES packages(id) ON DELETE RESTRICT,
    
    -- Информация о зависимости
    version_specified VARCHAR(100) NOT NULL,
    version_locked VARCHAR(100),
    status VARCHAR(50) DEFAULT 'ACTIVE',  -- ACTIVE, OUTDATED, VULNERABLE, DEPRECATED
    
    -- Риск и соответствие
    license_compliant BOOLEAN DEFAULT TRUE,
    has_vulnerabilities BOOLEAN DEFAULT FALSE,
    
    -- Временные метки
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    checked_at TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_project_deps_project_id ON project_dependencies(project_id);
CREATE INDEX idx_project_deps_package_id ON project_dependencies(package_id);
CREATE INDEX idx_project_deps_status ON project_dependencies(status);
CREATE UNIQUE INDEX idx_project_package_unique ON project_dependencies(project_id, package_id);
```

### 6. Package Dependency (Зависимости пакета)

Стеки зависимостей между пакетами.

```sql
CREATE TABLE package_dependencies (
    id BIGSERIAL PRIMARY KEY,
    package_id BIGINT NOT NULL REFERENCES packages(id) ON DELETE CASCADE,
    dependency_id BIGINT NOT NULL REFERENCES packages(id) ON DELETE RESTRICT,
    
    -- Информация о зависимости
    version_range VARCHAR(100),
    is_optional BOOLEAN DEFAULT FALSE,
    
    -- Временные метки
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_package_deps_package_id ON package_dependencies(package_id);
CREATE INDEX idx_package_deps_dependency_id ON package_dependencies(dependency_id);
CREATE UNIQUE INDEX idx_package_dependency_unique ON package_dependencies(package_id, dependency_id);
```

### 7. Vulnerability (Уязвимость)

Известные уязвимости в пакетах.

```sql
CREATE TABLE vulnerabilities (
    id BIGSERIAL PRIMARY KEY,
    package_id BIGINT NOT NULL REFERENCES packages(id) ON DELETE CASCADE,
    
    -- Информация об уязвимости
    cve_id VARCHAR(50) UNIQUE,
    severity VARCHAR(50),  -- CRITICAL, HIGH, MEDIUM, LOW
    description TEXT,
    
    -- Диапазон версий
    affected_versions_from VARCHAR(100),
    affected_versions_to VARCHAR(100),
    patched_versions VARCHAR(500),
    
    -- Источник информации
    source_url VARCHAR(500),
    
    -- Временные метки
    published_at TIMESTAMP,
    discovered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_vulnerabilities_package_id ON vulnerabilities(package_id);
CREATE INDEX idx_vulnerabilities_severity ON vulnerabilities(severity);
CREATE INDEX idx_vulnerabilities_cve_id ON vulnerabilities(cve_id);
```

### 8. Health Check Log (Логи проверок)

История обновлений и проверок пакетов.

```sql
CREATE TABLE health_check_logs (
    id BIGSERIAL PRIMARY KEY,
    package_id BIGINT NOT NULL REFERENCES packages(id) ON DELETE CASCADE,
    
    -- Результаты проверки
    health_score INTEGER,
    actuality_score INTEGER,
    activity_score INTEGER,
    repository_score INTEGER,
    community_score INTEGER,
    
    -- Метаданные
    source VARCHAR(100),  -- PYPI_API, DOCKER_HUB, GITHUB
    status VARCHAR(50),  -- SUCCESS, FAILED, TIMEOUT
    error_message TEXT,
    
    -- Временные метки
    checked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_health_check_package_id ON health_check_logs(package_id);
CREATE INDEX idx_health_check_checked_at ON health_check_logs(checked_at DESC);
```

---

## Диаграмма ER

```
┌─────────────────────────────────────────────────────────────────┐
│                        PACKAGES                                  │
├─────────────────────────────────────────────────────────────────┤
│ PK: id                                                           │
│ Fields: name, version, description, source_type, license       │
│         health_score, downloads_count, etc                      │
└───────────┬──────────────────────────────┬──────────────────────┘
            │                              │
            │ 1:N                          │ N:1
            ▼                              ▼
┌──────────────────────┐        ┌──────────────────────┐
│PACKAGE_DEPENDENCIES │        │     LICENSES         │
├──────────────────────┤        ├──────────────────────┤
│ PK: id               │        │ PK: id               │
│ FK: package_id       │        │ name, classification│
│ FK: dependency_id    │        └──────────────────────┘
└──────────────────────┘                 │
                                         │ N:N
                                         ▼
                        ┌──────────────────────────────┐
                        │LICENSE_COMPATIBILITY         │
                        ├──────────────────────────────┤
                        │ PK: id                       │
                        │ FK: license_id_1             │
                        │ FK: license_id_2             │
                        │ is_compatible                │
                        └──────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                        PROJECTS                                  │
├─────────────────────────────────────────────────────────────────┤
│ PK: id                                                           │
│ Fields: name, description, status, created_at                  │
└─────────────────┬──────────────────────────────────────────────┘
                  │
                  │ 1:N
                  ▼
        ┌──────────────────────┐
        │PROJECT_DEPENDENCIES  │
        ├──────────────────────┤
        │ PK: id               │
        │ FK: project_id       │
        │ FK: package_id       │
        │ status, risks        │
        └──────────────────────┘
                  │
                  │ N:1
                  ▼
         ┌─────────────────────┐
         │  PACKAGES           │
         └─────────────────────┘

┌──────────────────────────────────────────────────────────────────┐
│                   VULNERABILITIES                                │
├──────────────────────────────────────────────────────────────────┤
│ PK: id                                                            │
│ FK: package_id                                                   │
│ cve_id, severity, affected_versions                             │
└──────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────┐
│                   HEALTH_CHECK_LOGS                              │
├──────────────────────────────────────────────────────────────────┤
│ PK: id                                                            │
│ FK: package_id                                                   │
│ health_score, status, checked_at                                │
└──────────────────────────────────────────────────────────────────┘
```

---

## Миграции

### Система миграций (Flyway)

Миграции хранятся в `backend/src/main/resources/db/migration/`

```
db/migration/
├── V1__Create_packages_table.sql
├── V2__Create_licenses_table.sql
├── V3__Create_license_compatibility.sql
├── V4__Create_projects_table.sql
├── V5__Create_project_dependencies.sql
├── V6__Create_package_dependencies.sql
├── V7__Create_vulnerabilities.sql
├── V8__Create_health_check_logs.sql
└── V9__Add_indexes_and_constraints.sql
```

### Версионирование миграций

```
V<number>__<description>.sql

V1__Create_packages_table.sql
V2__Create_licenses_table.sql (выполнится после V1)
V3__Add_indexes.sql (выполнится после V2)
```

**Правила:**

- Номера должны быть в порядке возрастания
- Разделитель: `__` (два подчеркивания)
- Один скрипт = одна миграция
- Миграции нельзя менять после применения

### Пример миграции

```sql
-- V1__Create_packages_table.sql
CREATE TABLE packages (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    version VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_packages_name ON packages(name);
```

### Отката миграций

Flyway **не поддерживает откаты** (по дизайну). Для отката нужно создать новую миграцию:

```sql
-- V10__Rollback_previous_change.sql
DROP TABLE IF EXISTS old_table;

-- Или обратное изменение
ALTER TABLE packages DROP COLUMN IF EXISTS old_column;
```

---

## Обслуживание

### Резервное копирование

```bash
# Полный дамп базы
docker-compose exec postgres pg_dump -U postgres stackscout > backup.sql

# Восстановление из дампа
docker-compose exec -T postgres psql -U postgres stackscout < backup.sql

# Резервная копия с компрессией
docker-compose exec postgres pg_dump -U postgres stackscout | gzip > backup.sql.gz
```

### Выполнение запросов

```bash
# Интерактивный режим
docker-compose exec postgres psql -U postgres -d stackscout

# Выполнение одной команды
docker-compose exec -T postgres psql -U postgres -d stackscout -c "SELECT * FROM packages LIMIT 10;"

# Выполнение скрипта
docker-compose exec -T postgres psql -U postgres -d stackscout < script.sql
```

### Основные запросы

#### Получить все пакеты

```sql
SELECT id, name, version, health_score, license, created_at
FROM packages
ORDER BY health_score DESC
LIMIT 20;
```

#### Получить зависимости проекта

```sql
SELECT p.name, p.version, p.health_score, pd.status
FROM project_dependencies pd
JOIN packages p ON pd.package_id = p.id
WHERE pd.project_id = 1
ORDER BY p.health_score DESC;
```

#### Пакеты с уязвимостями

```sql
SELECT DISTINCT p.id, p.name, v.cve_id, v.severity
FROM packages p
JOIN vulnerabilities v ON p.id = v.package_id
WHERE v.severity IN ('CRITICAL', 'HIGH')
ORDER BY v.severity DESC, p.name;
```

#### Проверка совместимости лицензий в проекте

```sql
SELECT DISTINCT l1.name as license_1, l2.name as license_2, lc.is_compatible
FROM project_dependencies pd1
JOIN project_dependencies pd2 ON pd1.project_id = pd2.project_id
  AND pd1.id < pd2.id
JOIN packages p1 ON pd1.package_id = p1.id
JOIN packages p2 ON pd2.package_id = p2.id
JOIN licenses l1 ON p1.license = l1.name
JOIN licenses l2 ON p2.license = l2.name
LEFT JOIN license_compatibility lc ON 
  (lc.license_id_1 = l1.id AND lc.license_id_2 = l2.id) OR
  (lc.license_id_1 = l2.id AND lc.license_id_2 = l1.id)
WHERE pd1.project_id = 1
  AND lc.is_compatible = FALSE;
```

#### Статистика по здоровью пакетов

```sql
SELECT 
    COUNT(*) as total_packages,
    AVG(health_score) as avg_health,
    MAX(health_score) as max_health,
    MIN(health_score) as min_health,
    COUNT(CASE WHEN health_score >= 80 THEN 1 END) as healthy,
    COUNT(CASE WHEN health_score < 80 AND health_score >= 50 THEN 1 END) as moderate,
    COUNT(CASE WHEN health_score < 50 THEN 1 END) as unhealthy
FROM packages;
```

### Оптимизация

#### Размер БД

```bash
# Просмотр размера базы
docker-compose exec postgres psql -U postgres -d stackscout -c "
SELECT 
    schemaname,
    tablename,
    pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) AS size
FROM pg_tables
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;
"
```

#### Анализ запросов

```sql
-- Включить EXPLAIN
EXPLAIN ANALYZE
SELECT * FROM packages WHERE health_score > 80;

-- Проверить использование индекса
EXPLAIN (ANALYZE, BUFFERS)
SELECT * FROM packages WHERE name = 'requests';
```

#### Вакуумирование и анализ

```bash
# Очистка неиспользуемого пространства
docker-compose exec -T postgres psql -U postgres -d stackscout -c "VACUUM ANALYZE;"

# Только для таблицы
docker-compose exec -T postgres psql -U postgres -d stackscout -c "VACUUM ANALYZE packages;"
```

### Мониторинг

#### Активные подключения

```bash
docker-compose exec -T postgres psql -U postgres -d stackscout -c "
SELECT 
    pid,
    usename,
    application_name,
    state,
    query
FROM pg_stat_activity
WHERE datname = 'stackscout';
"
```

#### Медленные запросы

```sql
-- Включить логирование медленных запросов
-- В postgresql.conf:
-- slow_query_log_min_duration = 1000  -- 1 сек

-- Просмотр логов
SELECT * FROM pg_stat_statements
ORDER BY total_time DESC
LIMIT 10;
```

#### Блокировки

```bash
docker-compose exec -T postgres psql -U postgres -d stackscout -c "
SELECT * FROM pg_locks
WHERE NOT granted;
"
```

---

## Развитие схемы

### Планы на будущее

- [ ] Полнотекстовый поиск с использованием `tsvector`
- [ ] Партиционирование таблицы `health_check_logs` по датам
- [ ] Материализованные представления для сложных аналитик
- [ ] Кэширование результатов через Redis
- [ ] Event sourcing для истории изменений

### Неиспользуемые таблицы (для удаления)

```sql
-- Удалить таблицы при необходимости
DROP TABLE IF EXISTS table_name CASCADE;
```

---

## Дополнительные ресурсы

- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Flyway Migration Guide](https://flywaydb.org/documentation/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Backend README](../backend/README.md)
- [Architecture](./ARCHITECTURE.md)
