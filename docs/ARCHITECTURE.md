# Архитектура StackScout

<div align="center">

**Техническое описание системы и компонентов платформы управления Open Source библиотеками**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.10-6DB33F?logo=springboot)](https://spring.io/projects/spring-boot)
[![Next.js](https://img.shields.io/badge/Next.js-15-black?logo=nextdotjs)](https://nextjs.org/)

[Общая архитектура](#общая-архитектура) • [Backend](#backend) • [Frontend](#frontend) • [Данные](#данные) • [Deployment](#deployment)

</div>

---

## Общая архитектура

### Диаграмма системы

```
┌─────────────────────────────────────────────────────────────────┐
│                        Внешние источники                         │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────────┐   │
│  │  PyPI API    │    │ Docker Hub   │    │ GitHub API       │   │
│  └──────────────┘    └──────────────┘    └──────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                     StackScout Backend                           │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │                    REST API (Spring Web)                 │   │
│  │  /packages  /projects  /licenses  /health  /analytics   │   │
│  └──────────────────────────────────────────────────────────┘   │
│                              │                                    │
│  ┌──────────────┐  ┌─────────────────┐  ┌──────────────────┐   │
│  │  Service     │  │  Repository &   │  │  Scheduler &     │   │
│  │  Layer (бизн)│  │  Cache          │  │  Task             │   │
│  └──────────────┘  └─────────────────┘  └──────────────────┘   │
│                              │                                    │
│  ┌──────────────┐  ┌─────────────────┐  ┌──────────────────┐   │
│  │  Data        │  │  License        │  │  Health Score    │   │
│  │  Collector   │  │  Normalizer     │  │  Calculator      │   │
│  └──────────────┘  └─────────────────┘  └──────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
         │                        │                        │
         ▼                        ▼                        ▼
   ┌──────────┐          ┌──────────────┐         ┌──────────────┐
   │PostgreSQL│          │Redis Cache   │         │RabbitMQ      │
   │  (Data)  │          │(Sessions)    │         │(Tasks)       │
   └──────────┘          └──────────────┘         └──────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│                     StackScout Frontend                          │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │              Next.js + React (TypeScript)                │   │
│  │  Dashboard | Search | Projects | Licenses | Analytics   │   │
│  └──────────────────────────────────────────────────────────┘   │
│                              │                                    │
│  ┌──────────────┐  ┌─────────────────┐  ┌──────────────────┐   │
│  │  Components  │  │  API Service    │  │  State Mgmt      │   │
│  │  (MUI/Tail)  │  │  (axios/fetch)  │  │  (React Hooks)   │   │
│  └──────────────┘  └─────────────────┘  └──────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
         │
         └─────────────────────────────────────────────────────────┐
                                                                    │
                        ┌───────────────────────────┐              │
                        │  Браузер пользователя     │◀─────────────┘
                        └───────────────────────────┘
```

---

## Backend архитектура

### Слои приложения

```
┌─────────────────────────────────────────┐
│     API Layer (REST Controllers)        │
│  @RestController @RequestMapping        │
├─────────────────────────────────────────┤
│     Service Layer (Business Logic)      │
│  @Service с бизнес-логикой              │
├─────────────────────────────────────────┤
│    Repository Layer (Data Persistence)  │
│  Spring Data JPA Repository             │
├─────────────────────────────────────────┤
│     Domain Model (Entities)             │
│  @Entity, @Table, @Column               │
├─────────────────────────────────────────┤
│     Database Layer (PostgreSQL)         │
│  SQL, миграции Flyway                   │
└─────────────────────────────────────────┘
```

### Структура пакетов

```
com.stackscout/
├── api/
│   ├── controller/          # REST контроллеры
│   ├── dto/                 # Request/Response DTO
│   └── exception/           # Обработка ошибок
├── domain/
│   ├── entity/              # JPA сущности
│   ├── model/               # Доменные модели
│   └── enum/                # Перечисления
├── service/
│   ├── impl/                # Реализация сервисов
│   ├── collector/           # Сбор данных
│   └── analyzer/            # Анализ метрик
├── repository/              # Spring Data репозитории
├── config/                  # Конфигурация
│   ├── SecurityConfig       # Spring Security
│   ├── CacheConfig          # Redis кэш
│   └── SchedulingConfig     # Планировщик задач
├── util/                    # Утилиты
└── StackScoutApplication    # точка входа
```

### Ключевые компоненты

#### 1. **API Слой** (controller/)

Обработка HTTP запросов:

```java
@RestController
@RequestMapping("/api/v1/packages")
public class PackageController {
    @GetMapping
    public ResponseEntity<PageDto<PackageDto>> getPackages(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) { ... }
}
```

#### 2. **Service Слой** (service/)

Бизнес-логика и координация:

```java
@Service
public class PackageService {
    @Autowired
    private PackageRepository packageRepository;
    
    public PackageDto getPackageById(Long id) {
        Package pkg = packageRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException());
        return mapToDto(pkg);
    }
}
```

#### 3. **Data Collector** (service/collector/)

Асинхронный сбор данных из внешних источников:

```job
┌─────────────────────────────────────┐
│   Scheduled Task (Cron)             │
│   @Scheduled(cron = "0 0 * * * *")  │
├─────────────────────────────────────┤
│   1. Fetch from PyPI API            │
│   2. Fetch from Docker Hub          │
│   3. Normalize & Deduplicate        │
│   4. Store in Database              │
│   5. Calculate Health Scores        │
└─────────────────────────────────────┘
```

#### 4. **Health Score Calculator** (service/analyzer/)

Интеллектуальная система оценки:

```
Health Score = 
  Actuality (40%) +
  Activity (30%) +
  Repository (20%) +
  Community (10%)

Пример:
  Если последний релиз 5 дней назад → 40/40 баллов (актуально)
  Если 12 релизов за год → 30/30 баллов (активно)
  Если есть GitHub репо → 20/20 баллов (есть репо)
  Если 450 контрибьюторов → 10/10 баллов (активное сообщество)
  ═════════════════════════════════════
  Итого: 100/100 = Оценка 100+ (HEALTHY)
```

#### 5. **License Normalizer** (service/analyzer/)

Нормализация лицензионных данных:

```
MIT License
  ↓ (нормализация)
MIT

Apache License 2.0
  ↓
Apache-2.0

GNU General Public License v3
  ↓
GPL-3.0
```

### Кэширование (Redis)

```java
@Service
public class PackageService {
    @Cacheable(value = "packages", key = "#id")
    public PackageDto getPackageById(Long id) {
        return packageRepository.findById(id)
            .map(this::mapToDto)
            .orElseThrow();
    }
    
    @CacheEvict(value = "packages")
    public void updatePackage(PackageDto dto) { ... }
}
```

### Асинхронная обработка (RabbitMQ)

```
┌──────────────────────┐
│  Data Collector      │
│  (Scheduler)         │
└──────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│    RabbitMQ Queue                │
│  (collect_packages_queue)         │
└──────────────────────────────────┘
         │
         ▼
┌──────────────────────┐
│  Message Consumer    │
│  @RabbitListener     │
└──────────────────────┘
         │
         ▼
┌──────────────────────┐
│  Process & Store     │
│  PostgreSQL          │
└──────────────────────┘
```

---

## Frontend архитектура

### Структура проекта

```
frontend/
├── src/
│   ├── app/                    # App Router (Next.js 15)
│   │   ├── layout.tsx          # Root layout
│   │   ├── page.tsx            # Home page
│   │   ├── dashboard/          # Dashboard page
│   │   ├── explore/            # Package explorer
│   │   └── about/              # About page
│   ├── components/             # React компоненты
│   │   ├── layout/             # Layout компоненты
│   │   ├── ThemeRegistry.tsx   # MUI Theme Setup
│   │   └── ...                 # Другие компоненты
│   ├── lib/
│   │   └── api.ts              # API service/axios
│   ├── theme/
│   │   └── theme.ts            # MUI & Tailwind config
│   └── globals.css             # Global styles
├── public/                     # Static assets
├── package.json
├── tsconfig.json
├── next.config.ts
└── tailwind.config.ts
```

### Технологический стек

| Layer | Technology | Purpose |
|-------|-----------|---------|
| **Framework** | Next.js 15 | SSR/SSG, API routes |
| **UI Library** | React 19 | Components, hooks |
| **Styling** | Tailwind CSS 4 | Utility-first CSS |
| **UI Components** | Material UI 7 | Pre-built components |
| **HTTP Client** | axios | API calls |
| **State** | React Hooks | Component state |
| **Type Safety** | TypeScript | Type checking |
| **Linting** | ESLint | Code quality |

### Компонентная структура

```
App
├── ThemeRegistry
│   └── Layout
│       ├── Header
│       ├── Navigation
│       ├── Content
│       │   ├── Dashboard
│       │   │   ├── PackageCard
│       │   │   ├── HealthChart
│       │   │   └── MetricsGrid
│       │   ├── Explore
│       │   │   ├── SearchBar
│       │   │   ├── FilterPanel
│       │   │   └── PackageList
│       │   └── ProjectManager
│       └── Footer
```

### API интеграция

```typescript
// lib/api.ts
import axios from 'axios';

const apiClient = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL,
  headers: { 'Content-Type': 'application/json' }
});

export const getPackages = async (page = 0, size = 20) => {
  const response = await apiClient.get('/packages', {
    params: { page, size }
  });
  return response.data;
};
```

### Взаимодействие с Backend

```
Frontend                        Backend
   │                               │
   │ GET /api/v1/packages         │
   ├──────────────────────────────▶│
   │                               │
   │                ┌──────────────┤
   │                │ Fetch from DB
   │                │ Apply Filters
   │                │ Build Response
   │                └──────────────┤
   │          HTTP 200 + JSON      │
   │◀──────────────────────────────┤
   │                               │
   ├─ Parse JSON                   │
   ├─ Update State                 │
   ├─ Re-render Components         │
   ▼                               │
Browser                       Database
```

---

## Данные

### Модель данных

#### Package (Пакет)

```sql
packages
├── id (PK)
├── name
├── version
├── description
├── source_type (PYPI, DOCKER_HUB)
├── repository_url
├── health_score
├── license
├── downloads_count
├── last_update
└── created_at

package_dependencies
├── id (PK)
├── package_id (FK)
├── dependency_id (FK)
├── version_range
└── created_at
```

#### Project (Проект)

```sql
projects
├── id (PK)
├── name
├── description
├── created_at
└── updated_at

project_dependencies
├── id (PK)
├── project_id (FK)
├── package_id (FK)
├── version
├── status (ACTIVE, OUTDATED, VULNERABLE)
└── added_at
```

#### License (Лицензия)

```sql
licenses
├── id (PK)
├── name (SPDX identifier)
├── classification (PERMISSIVE, COPYLEFT, PROPRIETARY)
├── description
└── compatible_licenses (JSON)
```

### Миграции (Flyway)

```
db/migration/
├── V1__Create_packages_table.sql
├── V2__Create_projects_table.sql
├── V3__Create_licenses_table.sql
├── V4__Add_health_metrics.sql
└── V5__Create_dependencies_tables.sql
```

---

## Deployment

### Docker Compose

```yaml
version: "3.8"
services:
  # Backend
  app:
    build: ./backend
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/stackscout
      - SPRING_DATA_REDIS_HOST=redis
      - SPRING_RABBITMQ_HOST=rabbitmq
    depends_on:
      - postgres
      - redis
      - rabbitmq
  
  # Frontend
  frontend:
    build: ./frontend
    environment:
      - NEXT_PUBLIC_API_URL=http://app:8081/api/v1
    depends_on:
      - app
  
  # Infrastructure
  postgres: ...
  redis: ...
  rabbitmq: ...
```

### Production deployment

1. **Build Docker образы**
   ```bash
   docker-compose build
   ```

2. **Запуск контейнеров**
   ```bash
   docker-compose up -d
   ```

3. **Проверка здоровья**
   ```bash
   docker-compose ps
   ```

---

## Потоки данных

### 1. Сбор данных (Data Collection Flow)

```
Scheduler (Cron)
    │
    ▼
PyPI API ──────┐
Docker Hub ────┤──▶ Data Collector
GitHub ────────┘
    │
    ▼
Normalize & Validate
    │
    ▼
Deduplicate
    │
    ▼
Calculate Health Score
    │
    ▼
PostgreSQL (Store)
```

### 2. Запрос пакета (Query Flow)

```
Browser Request
    │
    ▼
API Controller
    │
    ▼
Redis Cache? ──YES──▶ Return Cached Data
    │
    NO
    │
    ▼
Service Layer
    │
    ▼
Database Query
    │
    ▼
Cache Result (Redis)
    │
    ▼
Response to Frontend
```

### 3. Анализ лицензий (License Analysis Flow)

```
User Add Package
    │
    ▼
Extract License Data
    │
    ▼
Normalize License Name
    │
    ▼
Check Compatibility Matrix
    │
    ▼
Generate Compliance Report
    │
    ▼
Store in Project
```

---

## Масштабируемость

### Горизонтальное масштабирование

```
┌──────────────────────┐
│   Load Balancer      │
│   (nginx/Traefik)    │
└──────────────────────┘
    │       │       │
    ▼       ▼       ▼
┌────┐  ┌────┐  ┌────┐
│App1│  │App2│  │App3│
└────┘  └────┘  └────┘
    │       │       │
    └───────┼───────┘
            ▼
      ┌──────────────┐
      │ PostgreSQL   │
      │ (Shared DB)  │
      └──────────────┘
```

### Кэширование

- **Redis** для сессий и кэша
- **CDN** для статических ассетов (в production)
- **Browser cache** для фронтенда

---

## Безопасность

### Backend

- **Spring Security** для аутентификации/авторизации
- **HTTPS** для транспорта
- **Input validation** на уровне DTO
- **SQL injection protection** через JPA параметризованные запросы
- **CORS** конфигурация

### Frontend

- **CSP** (Content Security Policy)
- **XSS Protection** встроенный в React
- **HTTPS only**
- **Secure cookies** для сессий

---

## Мониторинг и логирование

### Backend

```java
@Aspect
@Component
public class LoggingAspect {
    @Before("execution(* com.stackscout.service.*.*(..))")
    public void logBefore(JoinPoint jp) {
        logger.info("Entering: {}", jp.getSignature());
    }
}
```

### Metrics (Prometheus)

```yaml
# application.properties
management.endpoints.web.exposure.include=metrics,prometheus
management.metrics.enable.jvm=true
management.metrics.enable.process=true
```

### Visualization (Grafana)

Дашборды для:
- API response times
- Database query performance
- Cache hit rates
- System resources usage

---

## Дальнейшее развитие архитектуры

- [ ] Микросервисная архитектура (разделение по доменам)
- [ ] Message queue для асинхронных операций
- [ ] Кэширование результатов анализа
- [ ] GraphQL API в дополнение к REST
- [ ] WebSocket для real-time обновлений
- [ ] Kubernetes для оркестрации контейнеров
