<div align="center">

# StackScout

**Интеллектуальная платформа для анализа Open Source библиотек**

[![Coverage](https://img.shields.io/codecov/c/github/yourusername/stackscout)](https://codecov.io/gh/yourusername/stackscout)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-6DB33F?logo=springboot)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-007396?logo=openjdk)](https://openjdk.org/)

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

**StackScout** — это интеллектуальный микросервис на **Java Spring Boot** для **управления программными активами (Software Asset Management)**, который автоматизирует сбор, анализ и мониторинг open-source библиотек из PyPI и Docker Hub.

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

| Компонент            | Технология                   | Назначение                                 |
| -------------------- | ---------------------------- | ------------------------------------------ |
| **API слой**         | Spring Boot + Spring Web     | RESTful endpoints с JWT авторизацией       |
| **Слой данных**      | PostgreSQL + Spring Data JPA | Постоянное хранилище и ORM                 |
| **Слой кэширования** | Redis + Spring Cache         | Управление сессиями и ограничение запросов |
| **Система очередей** | Spring AMQP + RabbitMQ       | Асинхронный парсинг и планирование         |
| **Мониторинг**       | Prometheus + Grafana         | Сбор метрик и визуализация                 |

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

- JWT-авторизация (Spring Security)
- Управление доступом на основе ролей (RBAC)
- Ограничение количества запросов (100 запросов/мин на API ключ)
- Валидация входных данных с Bean Validation
- Спецификация OpenAPI 3.0 (SpringDoc)

### Наблюдаемость (Observability)

- Пользовательские метрики Prometheus (`http_requests_total`, `library_scan_duration`)
- Готовые дашборды Grafana
- Spring Boot Actuator для проверки здоровья (`/actuator/health`, `/actuator/prometheus`)
- Структурированное логирование с Logback

---

## Быстрый старт

### Требования

- **Java** 21 или выше
- **Git**

### Установка и запуск (без Docker)

Проект настроен для работы "из коробки" без дополнительных настроек!

1. **Клонируйте репозиторий**

   ```bash
   git clone https://github.com/NeonByte-Education/StackScout.git
   cd StackScout
   ```

2. **Запустите приложение**

   ```bash
   ./gradlew bootRun
   ```

   Приложение автоматически:
   - Использует встроенную H2 базу данных в памяти
   - Запустится на порту 8081
   - Отключит Redis и RabbitMQ
   - Настроит H2 консоль для просмотра данных

3. **Получите доступ к приложению**
   - API: http://localhost:8081
   - H2 Console: http://localhost:8081/h2-console
     - JDBC URL: `jdbc:h2:mem:stackscout`
     - Username: `sa`
     - Password: (оставить пустым)
   - Swagger документация: http://localhost:8081/swagger-ui.html
   - Actuator: http://localhost:8081/actuator

### Запуск с Docker (Production режим)

Для полноценного окружения с PostgreSQL, Redis и RabbitMQ:

1. **Запустите сервисы через Docker Compose**

   ```bash
   docker-compose up -d
   ```

2. **Запустите приложение с production профилем**

   ```bash
   ./gradlew bootRun --args='--spring.profiles.active=prod'
   ```

   Или без профиля (по умолчанию использует PostgreSQL):

   ```bash
   ./gradlew bootRun
   ```

### Режимы работы

#### Dev режим (по умолчанию)

- H2 база данных в памяти
- Без Redis и RabbitMQ
- H2 Console включена
- SQL логирование включено
- Автоматическое создание схемы БД

```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

#### Production режим

- PostgreSQL база данных
- Redis для кэширования
- RabbitMQ для очередей
- Flyway миграции
- Полный мониторинг

```bash
docker-compose up -d
./gradlew bootRun
```

### Режим разработки

```bash
# Сборка проекта
./gradlew build

# Запуск в режиме разработки с автоперезагрузкой
./gradlew bootRun

# Запуск тестов
./gradlew test

# Очистка и полная пересборка
./gradlew clean build

# Запуск с профилем
./gradlew bootRun --args='--spring.profiles.active=dev'
```

---

## API Документация

Интерактивная документация API доступна через **Swagger UI**:

**http://localhost:8080/swagger-ui.html**

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

- [x] Настройка проекта с Spring Boot
- [x] Конфигурация Docker окружения
- [x] Проектирование схемы БД (Spring Data JPA)
- [x] Базовые CRUD операции

### Неделя 4-6: Основные функции

- [x] Интеграция с PyPI API
- [x] Интеграция с Docker Hub API
- [x] Модуль нормализации лицензий
- [x] Калькулятор оценки здоровья

### Неделя 7-9: Продвинутые функции

- [ ] Система JWT-авторизации (Spring Security)
- [ ] Настройка очереди задач RabbitMQ
- [ ] Планировщик сборщика (@Scheduled)
- [ ] Слой кэширования Redis (Spring Cache)

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

- Java 17
- Spring Boot 3.2
- Spring Security
- Spring Data JPA

</td>
<td>

**База данных**

- PostgreSQL
- Flyway Migrations
- Redis
- RabbitMQ

</td>
<td>

**DevOps**

- Docker
- GitHub Actions
- Prometheus + Grafana
- Gradle (Kotlin DSL)

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

- Email: contact@stackscout.dev
- GitHub: [@S-NOWNUM-B](https://github.com/S-NOWNUM-B)
- GitHub: [@LINESKL](https://github.com/LINESKL)

---

<div align="center">

**Поставьте звезду этому репозиторию, если он вам помог!**

Сделано с любовью и Java

</div>
