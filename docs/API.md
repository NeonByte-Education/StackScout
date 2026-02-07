# StackScout REST API Документация

<div align="center">

**Полная документация API для платформы управления Open Source библиотеками**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.10-6DB33F?logo=springboot)](https://spring.io/projects/spring-boot)

[Аутентификация](#аутентификация) • [Пакеты](#пакеты) • [Проекты](#проекты) • [Лицензии](#лицензии)

</div>

---

## Базовая информация

- **Base URL**: `http://localhost:8081/api/v1`
- **Content-Type**: `application/json`
- **Формат ответов**: JSON

## Аутентификация

В текущей версии (MVP) API доступен без аутентификации. В будущих версиях будет реализована поддержка JWT токенов.

```bash
# Пример запроса с токеном (будущая версия)
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  http://localhost:8081/api/v1/packages
```

---

## Эндпоинты

### 🔍 Пакеты (Packages)

#### Получить список всех пакетов

```http
GET /api/v1/packages
```

**Query параметры:**

| Параметр | Тип | Описание | Пример |
| -------- | --- | ------- | ------ |
| `page` | integer | Номер страницы (начиная с 0) | `0` |
| `size` | integer | Количество результатов на странице | `20` |
| `sort` | string | Поле для сортировки | `healthScore,desc` |
| `search` | string | Поиск по названию или описанию | `django` |

**Пример запроса:**

```bash
curl "http://localhost:8081/api/v1/packages?page=0&size=20&sort=healthScore,desc"
```

**Пример ответа:**

```json
{
  "content": [
    {
      "id": 1,
      "name": "requests",
      "version": "2.31.0",
      "description": "A simple, yet elegant HTTP library for Python",
      "sourceType": "PYPI",
      "healthScore": 92,
      "lastUpdate": "2024-01-15T10:30:00Z",
      "downloads": 50000000,
      "license": "Apache-2.0"
    }
  ],
  "totalElements": 1250,
  "totalPages": 63,
  "currentPage": 0,
  "pageSize": 20
}
```

#### Получить информацию о конкретном пакете

```http
GET /api/v1/packages/{id}
```

**Параметры пути:**

| Параметр | Тип | Описание |
| -------- | --- | ------- |
| `id` | integer | Уникальный идентификатор пакета |

**Пример запроса:**

```bash
curl http://localhost:8081/api/v1/packages/1
```

**Пример ответа:**

```json
{
  "id": 1,
  "name": "requests",
  "version": "2.31.0",
  "description": "A simple, yet elegant HTTP library for Python",
  "sourceType": "PYPI",
  "repositoryUrl": "https://github.com/psf/requests",
  "healthScore": 92,
  "lastUpdate": "2024-01-15T10:30:00Z",
  "downloads": 50000000,
  "license": "Apache-2.0",
  "authors": ["Kenneth Reitz"],
  "dependencies": [
    {
      "id": 15,
      "name": "urllib3",
      "versionRange": ">=1.21.1,<3"
    }
  ],
  "metrics": {
    "actuality": 40,
    "activity": 30,
    "repository": 20,
    "community": 2
  }
}
```

#### Поиск пакетов по критериям

```http
POST /api/v1/packages/search
```

**Body параметры:**

```json
{
  "name": "django",
  "minHealthScore": 80,
  "licenseType": "PERMISSIVE",
  "sourceType": "PYPI"
}
```

---

### 📊 Проекты (Projects)

#### Получить список проектов

```http
GET /api/v1/projects
```

**Query параметры:**

| Параметр | Тип | Описание |
| -------- | --- | ------- |
| `page` | integer | Номер страницы |
| `size` | integer | Размер страницы |

**Пример ответа:**

```json
{
  "content": [
    {
      "id": 1,
      "name": "my-web-app",
      "description": "Web application project",
      "createdAt": "2024-01-01T12:00:00Z",
      "dependenciesCount": 45,
      "averageHealthScore": 85,
      "licenseRisks": 2
    }
  ],
  "totalElements": 5,
  "totalPages": 1,
  "currentPage": 0
}
```

#### Создать новый проект

```http
POST /api/v1/projects
```

**Body:**

```json
{
  "name": "my-project",
  "description": "Description of the project"
}
```

**Ответ (201 Created):**

```json
{
  "id": 1,
  "name": "my-project",
  "description": "Description of the project",
  "createdAt": "2024-01-20T15:30:00Z"
}
```

#### Получить информацию о проекте

```http
GET /api/v1/projects/{id}
```

**Пример ответа:**

```json
{
  "id": 1,
  "name": "my-project",
  "description": "Description of the project",
  "createdAt": "2024-01-20T15:30:00Z",
  "dependencies": [
    {
      "packageId": 1,
      "packageName": "requests",
      "version": "2.31.0",
      "healthScore": 92,
      "license": "Apache-2.0",
      "addedAt": "2024-01-20T15:35:00Z"
    }
  ]
}
```

#### Добавить зависимость к проекту

```http
POST /api/v1/projects/{projectId}/dependencies
```

**Body:**

```json
{
  "packageId": 1,
  "version": "2.31.0"
}
```

#### Удалить зависимость из проекта

```http
DELETE /api/v1/projects/{projectId}/dependencies/{dependencyId}
```

---

### 📜 Лицензии (Licenses)

#### Получить список всех лицензий

```http
GET /api/v1/licenses
```

**Пример ответа:**

```json
{
  "content": [
    {
      "id": 1,
      "name": "Apache-2.0",
      "classification": "PERMISSIVE",
      "description": "Apache License 2.0",
      "compatible": ["MIT", "BSD-3-Clause"],
      "incompatible": ["GPL-3.0"],
      "packages": 5200
    }
  ],
  "totalElements": 50,
  "totalPages": 3,
  "currentPage": 0
}
```

#### Проверить совместимость лицензий

```http
POST /api/v1/licenses/compatibility
```

**Body:**

```json
{
  "licenses": ["MIT", "Apache-2.0", "BSD-3-Clause"]
}
```

**Пример ответа:**

```json
{
  "compatible": true,
  "risks": [],
  "recommendations": []
}
```

---

### 🏥 Система здоровья (Health)

#### Получить метрики здоровья пакета

```http
GET /api/v1/packages/{id}/health
```

**Пример ответа:**

```json
{
  "packageId": 1,
  "packageName": "requests",
  "totalScore": 92,
  "metrics": {
    "actuality": {
      "score": 40,
      "label": "Новые релизы",
      "lastRelease": "2024-01-15T10:30:00Z",
      "daysAgo": 5
    },
    "activity": {
      "score": 30,
      "label": "Активная разработка",
      "releasesPerYear": 12
    },
    "repository": {
      "score": 20,
      "label": "Наличие репозитория",
      "url": "https://github.com/psf/requests"
    },
    "community": {
      "score": 2,
      "label": "Активность сообщества",
      "contributors": 450
    }
  },
  "healthStatus": "HEALTHY",
  "recommendations": [
    "Пакет активно поддерживается",
    "Регулярные обновления безопасности"
  ]
}
```

#### Получить общие метрики системы

```http
GET /api/v1/health/stats
```

**Пример ответа:**

```json
{
  "totalPackages": 128450,
  "totalProjects": 45,
  "averageHealthScore": 78,
  "packagesWithRisks": {
    "critical": 234,
    "high": 1245,
    "medium": 5400
  },
  "lastUpdate": "2024-01-20T16:30:00Z"
}
```

---

## Коды ошибок

| Код | Описание | Решение |
| --- | -------- | ------- |
| `200` | OK | Запрос выполнен успешно |
| `201` | Created | Ресурс создан успешно |
| `400` | Bad Request | Некорректные параметры запроса |
| `404` | Not Found | Ресурс не найден |
| `409` | Conflict | Конфликт данных (например, дублирование) |
| `500` | Internal Server Error | Ошибка сервера |

**Пример ошибки:**

```json
{
  "error": "Package not found",
  "statusCode": 404,
  "timestamp": "2024-01-20T16:30:00Z"
}
```

---

## Примеры использования

### Python (requests)

```python
import requests

# Получить пакет
response = requests.get(
  'http://localhost:8081/api/v1/packages/1'
)
package = response.json()

# Создать проект
project_data = {
  'name': 'my-project',
  'description': 'My awesome project'
}
response = requests.post(
  'http://localhost:8081/api/v1/projects',
  json=project_data
)
```

### JavaScript (fetch)

```javascript
// Получить список пакетов
const response = await fetch(
  'http://localhost:8081/api/v1/packages?size=20'
);
const packages = await response.json();

// Добавить зависимость
const addDependency = async (projectId, packageId) => {
  const response = await fetch(
    `http://localhost:8081/api/v1/projects/${projectId}/dependencies`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ packageId, version: '1.0.0' })
    }
  );
  return response.json();
};
```

### cURL

```bash
# Получить пакеты с фильтром
curl -X GET \
  'http://localhost:8081/api/v1/packages?page=0&size=10&sort=healthScore,desc' \
  -H 'Accept: application/json'

# Поиск пакетов
curl -X POST \
  'http://localhost:8081/api/v1/packages/search' \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "django",
    "minHealthScore": 80
  }'
```

---

## Rate Limiting (в разработке)

В будущих версиях будет реализовано ограничение частоты запросов:

- **Default**: 100 запросов в минуту для анонимных пользователей
- **Authenticated**: 1000 запросов в минуту для авторизованных

---

## Версионирование

Текущая версия API: **v1**

Формат: `/api/v1/...`

При появлении несовместимых изменений будет создана новая версия (`/api/v2/...`).

---

## Дополнительные ресурсы

- [Postman Collection](../Postman/postman-collection.json)
- [Backend README](../backend/README.md)
- [Быстрый старт](./Instructions%20for%20use/QUICKSTART.md)
