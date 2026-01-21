<div align="center">

# StackScout

**Интеллектуальная платформа для анализа Open Source библиотек**

[![Coverage](https://img.shields.io/codecov/c/github/yourusername/stackscout)](https://codecov.io/gh/yourusername/stackscout)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![NestJS](https://img.shields.io/badge/NestJS-v10.x-E0234E?logo=nestjs)](https://nestjs.com/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.x-3178C6?logo=typescript)](https://www.typescriptlang.org/)

[Возможности](#возможности) • [Архитектура](#архитектура) • [Быстрый старт](#быстрый-старт) • [API Документация](#api-документация) • [Roadmap](#roadmap)


</div>

---

## О проекте

### Проблема: Dependency Hell

Современная разработка программного обеспечения во многом зависит от сторонних библиотек. Однако выбор правильной зависимости представляет серьезную проблему:

- **Уязвимости безопасности**: Устаревшие или заброшенные пакеты
- **Конфликты лицензий**: Несовместимые условия лицензирования (GPL vs MIT)
- **Риск поддержки**: Библиотеки без недавних обновлений
- **Плохая документация**: Отсутствие информации перед внедрением

### Решение: StackScout

**StackScout** — это интеллектуальный микросервис для **управления программными активами (Software Asset Management)**, который автоматизирует сбор, анализ и мониторинг open-source библиотек из PyPI и Docker Hub.

Наша платформа предоставляет:
- Оценку "здоровья" библиотек в реальном времени
- Валидацию соответствия лицензиям
- Автоматический сбор метаданных
- Полнофункциональный REST API с аналитикой

---

## Архитектура

```
┌──────────────────┐      ┌──────────────────┐      ┌──────────────────┐
│ Источники данных │      │   StackScout     │      │     Клиенты      │
│                  │      │   Микросервис    │      │                  │
│  • PyPI API      │─────▶│                  │◀─────│  • Веб-приложение│
│  • Docker Hub    │      │  ┌────────────┐  │      │  • CLI утилиты   │
│                  │      │  │ Коллектор  │  │      │  • Интеграции    │
└──────────────────┘      │  └──────┬─────┘  │      └──────────────────┘
                          │         │        │
                          │  ┌──────▼─────┐  │
                          │  │ PostgreSQL │  │
┌──────────────────┐      │  └──────┬─────┘  │      ┌──────────────────┐
│   Мониторинг     │      │         │        │      │  Очередь задач   │
│                  │◀─────│  ┌──────▼─────┐  │─────▶│                  │
│  • Prometheus    │      │  │  REST API  │  │      │  • Redis         │
│  • Grafana       │      │  └────────────┘  │      │  • BullMQ        │
└──────────────────┘      └──────────────────┘      └──────────────────┘
```

### Основные компоненты

| Компонент | Технология | Назначение |
|-----------|-----------|-----------|
| **API слой** | NestJS + Express | RESTful endpoints с JWT авторизацией |
| **Слой данных** | PostgreSQL + Prisma/TypeORM | Постоянное хранилище и ORM |
| **Слой кэширования** | Redis | Управление сессиями и ограничение запросов |
| **Система очередей** | BullMQ | Асинхронный парсинг и планирование |
| **Мониторинг** | Prometheus + Grafana | Сбор метрик и визуализация |

---

## Возможности

### Сборщик данных (Data Collector)
- Автоматический сбор метаданных через официальные API (PyPI JSON API, Docker Registry API)
- Планируемый парсинг через Cron jobs (настраиваемые интервалы)
- Параллельная обработка с использованием очередей задач
- Нормализация и дедупликация данных

### Модуль лицензионного соответствия
- Нормализация названий лицензий (`"MIT License"` → `"MIT"`)
- Классификация рисков:
  - **Разрешающие**: MIT, Apache-2.0, BSD
  - **Copyleft**: GPL, AGPL, MPL
  - **Проприетарные**: Пользовательские лицензии
- Матрица совместимости для проектов с множественными лицензиями

### Алгоритм оценки здоровья
Интеллектуальная система оценки на основе:
- **Актуальность**: Время с момента последнего релиза (0-40 баллов)
- **Активность**: Количество релизов за последний год (0-30 баллов)
- **Репозиторий**: Наличие официального исходного кода (0-20 баллов)
- **Сообщество**: Количество контрибьюторов и активность (0-10 баллов)

**Интерпретация оценок**:
- **80-100**: Отлично (Готово к production)
- **60-79**: Хорошо (Рекомендуется проверка)
- **40-59**: Удовлетворительно (Использовать с осторожностью)
- **0-39**: Плохо (Избегать или заменить)

### Защищенный API
- JWT-авторизация
- Управление доступом на основе ролей (RBAC)
- Ограничение количества запросов (100 запросов/мин на API ключ)
- Валидация входных данных с class-validator
- Спецификация OpenAPI 3.0

### Наблюдаемость (Observability)
- Пользовательские метрики Prometheus (`http_requests_total`, `library_scan_duration`)
- Готовые дашборды Grafana
- Endpoints для проверки здоровья (`/health`, `/metrics`)
- Структурированное логирование с Winston

---

## Быстрый старт

### Требования

- **Node.js** >= 18.x
- **Docker** & Docker Compose
- **Git**

### Установка

1. **Клонируйте репозиторий**
   ```bash
   git clone https://github.com/NeonByte-Education/StackScout.git
   cd StackScout
   ```

2. **Настройте переменные окружения**
   ```bash
   cp .env.example .env
   ```

   Отредактируйте `.env` файл:
   ```env
   # База данных
   DATABASE_URL="postgresql://postgres:password@localhost:5432/stackscout"
   
   # Redis
   REDIS_HOST=localhost
   REDIS_PORT=6379
   
   # JWT
   JWT_SECRET=your-super-secret-key
   JWT_EXPIRATION=7d
   
   # API ключи
   PYPI_API_URL=https://pypi.org/pypi
   DOCKERHUB_API_URL=https://hub.docker.com/v2
   
   # Расписание Cron
   COLLECTOR_CRON="0 */6 * * *"  # Каждые 6 часов
   ```

3. **Запустите сервисы через Docker Compose**
   ```bash
   docker-compose up -d
   ```

   Это запустит:
   - API сервер (Порт 3000)
   - PostgreSQL (Порт 5432)
   - Redis (Порт 6379)
   - Prometheus (Порт 9090)
   - Grafana (Порт 3001)

4. **Выполните миграции базы данных**
   ```bash
   npm run prisma:migrate
   # или
   npm run typeorm:migrate
   ```

5. **Получите доступ к приложению**
   - API: http://localhost:3000
   - Swagger документация: http://localhost:3000/api/docs
   - Grafana: http://localhost:3001 (admin/admin)

### Режим разработки

```bash
# Установка зависимостей
npm install

# Запуск в режиме отслеживания изменений
npm run start:dev

# Запуск тестов
npm run test
npm run test:e2e
npm run test:cov
```

---

## API Документация

Интерактивная документация API доступна через **Swagger UI**:

**http://localhost:3000/api/docs**

### Примеры endpoints

#### Поиск библиотек
```http
GET /api/v1/libraries/search?query=nestjs&source=npm
Authorization: Bearer <your-jwt-token>
```

#### Получение деталей библиотеки
```http
GET /api/v1/libraries/:id
Authorization: Bearer <your-jwt-token>
```

Ответ:
```json
{
  "id": "uuid-here",
  "name": "@nestjs/core",
  "version": "10.3.0",
  "source": "npm",
  "license": "MIT",
  "healthScore": 95,
  "lastRelease": "2024-01-15T10:30:00Z",
  "repository": "https://github.com/nestjs/nest"
}
```

#### Запуск ручного сканирования
```http
POST /api/v1/collector/scan
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "source": "pypi",
  "packages": ["requests", "django"]
}
```

---

## Roadmap

### Неделя 1-3: Фундамент
- [ ] Настройка проекта с NestJS
- [ ] Конфигурация Docker окружения
- [ ] Проектирование схемы БД (Prisma/TypeORM)
- [ ] Базовые CRUD операции

### Неделя 4-6: Основные функции
- [ ] Интеграция с PyPI API
- [ ] Интеграция с Docker Hub API
- [ ] Модуль нормализации лицензий
- [ ] Калькулятор оценки здоровья

### Неделя 7-9: Продвинутые функции
- [ ] Система JWT-авторизации
- [ ] Настройка очереди задач BullMQ
- [ ] Планировщик сборщика на Cron
- [ ] Слой кэширования Redis

### Неделя 10-12: Качество & DevOps
- [ ] Unit & E2E тестирование (Jest)
- [ ] CI/CD пайплайн GitHub Actions
- [ ] Интеграция метрик Prometheus
- [ ] Дашборды Grafana

### Неделя 13-15: Готовность к production
- [ ] Ограничение частоты запросов API
- [ ] Полная документация
- [ ] Оптимизация производительности
- [ ] Аудит безопасности



---

## Технологический стек

<table>
<tr>
<td>

**Backend**
- TypeScript
- Node.js
- NestJS

</td>
<td>

**База данных**
- PostgreSQL
- Prisma ORM / TypeORM
- Redis

</td>
<td>

**DevOps**
- Docker
- GitHub Actions
- Prometheus + Grafana

</td>
</tr>
</table>

---

## Вклад в проект

Мы приветствуем ваш вклад! Следуйте этим шагам:

1. Сделайте Fork репозитория
2. Создайте ветку для новой функции (`git checkout -b feature/amazing-feature`)
3. Зафиксируйте изменения (`git commit -m 'Add amazing feature'`)
4. Отправьте в ветку (`git push origin feature/amazing-feature`)
5. Откройте Pull Request

Пожалуйста, убедитесь, что:
- Все тесты проходят (`npm run test`)
- Код соответствует правилам ESLint (`npm run lint`)
- Сообщения коммитов следуют [Conventional Commits](https://www.conventionalcommits.org/)

---

## Лицензия

Этот проект лицензирован под **MIT License** - подробности в файле [LICENSE](LICENSE).

---

## Авторы

**StackScout Team**  
Проект по Advanced Backend & DevOps

**Контакты:**
- GitHub: [@S-NOWNUM-B](https://github.com/S-NOWNUM-B)
- GitHub: [@LINESKL](https://github.com/LINESKL)

---

<div align="center">

**Поставьте звезду этому репозиторию, если он вам помог!**

Сделано с любовью и TypeScript

</div>
