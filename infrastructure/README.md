# StackScout Infrastructure Documentation

Папка содержит конфигурации для инфраструктурных компонентов.

## Структура

### docker/
Конфигурации для Docker контейнеров:

- **postgres/init.sql** - Инициализация PostgreSQL
  - Создание таблиц (Packages, Licenses, Projects, Dependencies, Vulnerabilities)
  - Создание индексов
  - Вставка стандартных лицензий
  - Установка триггеров для обновления временных меток

- **redis/redis.conf** - Конфигурация Redis кэша
  - Параметры памяти (maxmemory: 256mb)
  - RDB persistence
  - Keyspace notifications для сессий

### monitoring/

#### prometheus/prometheus.yml
- Конфигурация Prometheus для сбора метрик
- Job'ы для:
  - Самого Prometheus
  - Spring Boot приложения (StackScout Backend)
  - PostgreSQL (через экспортер)
  - Redis (через экспортер)
  - RabbitMQ

#### grafana/
- **provisioning/datasources.yml** - Конфигурация источников данных
  - Prometheus как основной источник
  - Backend API как отдельный источник

- **provisioning/dashboards.yml** - Конфигурация дашбордов
  - Подключение файлов дашбордов

- **provisioning/grafana.ini** - Конфигурация Grafana
  - Параметры сервера
  - Интеграция с PostgreSQL
  - Учетные данные администратора

- **dashboards/backend-metrics.json** - Дашборд метрик Backend
  - HTTP Request Rate
  - Total Requests
  - JVM Memory Usage
  - JVM Thread Count

## Использование

### Для локальной разработки

Все конфиги уже интегрированы в docker-compose:

```bash
docker-compose up -d
```

### Доступ

- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (admin/admin)
- Redis: localhost:6379
- PostgreSQL: localhost:5432 (postgres/postgres)

## Развитие

- [ ] Добавить экспортеры для Postgres и Redis
- [ ] Создать кастомные метрики для бизнес-логики
- [ ] Настроить alerting правила
- [ ] Добавить темплейты для дашбордов
