<div align="center">

# StackScout - Postman Collection

**Готовые запросы для тестирования REST API платформы анализа Open Source библиотек**

[![Postman](https://img.shields.io/badge/Postman-v10-FF6C37?logo=postman)](https://www.postman.com/)
[![API](https://img.shields.io/badge/API-v1-4CAF50?logo=api)](../API.md)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.10-6DB33F?logo=springboot)](https://spring.io/projects/spring-boot)

[Установка](#установка) • [Структура](#структура-коллекции) • [Примеры](#примеры-использования) • [Аутентификация](#аутентификация)

</div>

---

## О коллекции

**Postman Collection** содержит полный набор готовых HTTP запросов для всех API эндпоинтов StackScout. Это идеальный инструмент для:

- **Тестирования API** локально или в production
- **Обучения** структуре данных и API методов
- **Автоматизации** повторяющихся запросов
- **Документирования** примеры использования

---

## Установка

### 1. Скачать Postman

Если у вас еще нет Postman, скачайте его с [официального сайта](https://www.postman.com/downloads/).

### 2. Импортировать коллекцию

#### Способ A: Через UI

1. Откройте Postman
2. Нажмите **Collections** в левой панели
3. Нажмите **Import** → **Upload Files**
4. Выберите файл `postman-collection.json`
5. Коллекция появится в Collections

#### Способ B: Через меню File

1. **File** → **Import**
2. Выберите вкладку **Upload Files**
3. Найдите и откройте `postman-collection.json`
4. Нажмите **Import**

#### Способ C: Через drag & drop

Просто перетащите файл `postman-collection.json` в окно Postman.

---

## Структура коллекции

```
StackScout API
├── Authentication
│   ├── Register           (POST /api/v1/auth/register) - Публичная регистрация
│   ├── Login              (POST /api/v1/auth/login)    - Получить токен
│   ├── Refresh Token      (POST /api/v1/auth/refresh)  - Обновить токен (требует токен)
│   └── Logout             (POST /api/v1/auth/logout)   - Выйти из системы (требует токен)
│
├── Health & Status
│   ├── Health Check      (GET /api/v1/health)         - Проверка статуса
│   └── Ping              (GET /api/v1/ping)           - Ping
│
├── Libraries
│   ├── Get All           (GET /api/v1/libraries)
│   ├── Get by ID         (GET /api/v1/libraries/{id})
│   ├── Search            (GET /api/v1/libraries/search)
│   ├── Get Healthy       (GET /api/v1/libraries/healthy)
│   ├── Get Stats         (GET /api/v1/libraries/stats)
│   ├── Create            (POST /api/v1/libraries) - Требует токен (ADMIN)
│   ├── Update            (PUT /api/v1/libraries/{id}) - Требует токен (ADMIN)
│   └── Delete            (DELETE /api/v1/libraries/{id}) - Требует токен (ADMIN)
│
├── Licenses
│   ├── Get All           (GET /api/v1/licenses)
│   ├── Get by ID         (GET /api/v1/licenses/{id})
│   ├── Search            (GET /api/v1/licenses/search)
│   ├── Get by Type       (GET /api/v1/licenses/type/{type})
│   ├── Get OSI Approved  (GET /api/v1/licenses/osi-approved)
│   ├── Create            (POST /api/v1/licenses) - Требует токен (ADMIN)
│   ├── Update            (PUT /api/v1/licenses/{id}) - Требует токен (ADMIN)
│   └── Delete            (DELETE /api/v1/licenses/{id}) - Требует токен (ADMIN)
│
├── Scan Jobs
│   ├── Get All           (GET /api/v1/scan-jobs)
│   ├── Get by ID         (GET /api/v1/scan-jobs/{id})
│   ├── Get by Status     (GET /api/v1/scan-jobs/status/{status})
│   ├── Get by Source     (GET /api/v1/scan-jobs/source/{source})
│   ├── Get Recent        (GET /api/v1/scan-jobs/recent)
│   ├── Get Statistics    (GET /api/v1/scan-jobs/statistics)
│   ├── Create            (POST /api/v1/scan-jobs) - Требует токен (ADMIN)
│   ├── Update Status     (PATCH /api/v1/scan-jobs/{id}/status) - Требует токен (ADMIN)
│   └── Delete            (DELETE /api/v1/scan-jobs/{id}) - Требует токен (ADMIN)
│
├── Collector
│   ├── Start Scan        (POST /api/v1/collector/scan) - Требует токен (ADMIN)
│   └── Get Status        (GET /api/v1/collector/status)
│
└── Users
    ├── Account Management (для текущего пользователя)
    │   ├── Get Profile              (GET /api/v1/users/profile)
    │   ├── Update Profile           (PUT /api/v1/users/profile)
    │   ├── Change Password          (POST /api/v1/users/change-password)
    │   └── Delete Account           (DELETE /api/v1/users/profile)
    │
    └── Admin Management (требует роль ADMIN)
        ├── Get All Users            (GET /api/v1/users)
        ├── Get User by ID           (GET /api/v1/users/{id})
        ├── Create User              (POST /api/v1/users)
        ├── Update User              (PUT /api/v1/users/{id})
        └── Delete User              (DELETE /api/v1/users/{id})
```

---

## Примеры использования

### 0. Регистрация и создание аккаунта

#### Регистрация нового пользователя

```
POST /api/v1/auth/register
Content-Type: application/json

{
  "username": "newuser",
  "email": "newuser@stackscout.io",
  "password": "SecurePassword123!",
  "confirmPassword": "SecurePassword123!"
}
```

**Ожидаемый ответ:**
```json
{
  "id": 5,
  "username": "newuser",
  "email": "newuser@stackscout.io",
  "roles": ["USER"],
  "isActive": true,
  "createdAt": "2026-02-07T13:04:00Z"
}
```

#### Логин и получение токена

```
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "newuser",
  "password": "SecurePassword123!"
}
```

**Ожидаемый ответ:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600,
  "tokenType": "Bearer"
}
```

#### Отредактировать свой профиль

```
PUT /api/v1/users/profile
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "newemail@stackscout.io"
}
```

#### Изменить пароль

```
POST /api/v1/users/change-password
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "oldPassword": "SecurePassword123!",
  "newPassword": "NewPassword456!",
  "confirmPassword": "NewPassword456!"
}
```

#### Удалить свой аккаунт

```
DELETE /api/v1/users/profile
Authorization: Bearer {{token}}
```

---

### 1. Проверка здоровья API

Самый простой способ убедиться, что API работает:

```
GET http://localhost:8081/api/v1/health
```

**Ожидаемый ответ:**
```json
{
  "status": "UP",
  "timestamp": "2026-02-07T13:04:00Z"
}
```

### 2. Получить список всех библиотек

```
GET http://localhost:8081/api/v1/libraries?page=0&size=10
```

**Query параметры:**
- `page` — номер страницы (начиная с 0)
- `size` — количество результатов на странице
- `sort` — сортировка (например: `healthScore,desc`)

**Пример ответа:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "requests",
      "version": "2.31.0",
      "sourceType": "PYPI",
      "healthScore": 92,
      "license": "Apache-2.0"
    }
  ],
  "totalElements": 1250,
  "totalPages": 63,
  "currentPage": 0
}
```

### 3. Поиск библиотеки по критериям

```
POST http://localhost:8081/api/v1/libraries/search
Content-Type: application/json

{
  "name": "django",
  "minHealthScore": 80,
  "sourceType": "PYPI"
}
```

### 4. Запустить сканирование

```
POST http://localhost:8081/api/v1/scan-jobs
Content-Type: application/json
Authorization: Bearer YOUR_TOKEN

{
  "name": "PyPI Scan 2026-02",
  "sourceType": "PYPI"
}
```

**Ответ:**
```json
{
  "id": 1,
  "name": "PyPI Scan 2026-02",
  "sourceType": "PYPI",
  "status": "PENDING",
  "progress": 0,
  "createdAt": "2026-02-07T13:04:00Z"
}
```

### 5. Получить результаты сканирования

```
GET http://localhost:8081/api/v1/scan-jobs/1/results
```

---

## Аутентификация

### Текущий статус

Коллекция поддерживает **JWT Bearer Token** авторизацию для защищенных эндпоинтов. Папка **Authentication** содержит все необходимые запросы для работы с токенами.

### Защищённые эндпоинты (требующие токена)

**Уровень ADMIN** (требуется роль администратора):
- `POST /api/v1/libraries` — создание библиотеки
- `PUT /api/v1/libraries/{id}` — обновление библиотеки
- `DELETE /api/v1/libraries/{id}` — удаление библиотеки
- `POST /api/v1/scan-jobs` — запуск сканирования
- `PATCH /api/v1/scan-jobs/{id}/status` — изменение статуса сканирования
- `POST /api/v1/collector/scan` — запуск сборщика
- `POST /api/v1/users` — создание пользователя
- `PUT /api/v1/users/{id}` — обновление пользователя
- `DELETE /api/v1/users/{id}` — удаление пользователя

**Уровень USER** (требуется аутентификация):
- `GET /api/v1/users` — получить список пользователей
- `GET /api/v1/users/{id}` — получить информацию о пользователе

**Публичные эндпоинты** (без авторизации):
- `GET /api/v1/health` — проверка здоровья
- `GET /api/v1/ping` — ping
- `GET /api/v1/libraries` — получить список библиотек
- `GET /api/v1/licenses` — получить список лицензий
- `GET /api/v1/scan-jobs` — и другие GET запросы

### Процесс получения токена

#### Шаг 1: Выполнить Login запрос

Откройте запрос **Authentication → Login** и нажмите **Send**:

```
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "password"
}
```

**Ожидаемый ответ:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600,
  "tokenType": "Bearer"
}
```

#### Шаг 2: Использовать токен в запросах

Благодаря скрипту в Tests секции, токен автоматически сохранится в переменной `{{token}}`. 

Все защищенные запросы уже содержат заголовок:
```
Authorization: Bearer {{token}}
```

#### Шаг 3: Обновить токен (если истек)

Используйте запрос **Authentication → Refresh Token** для получения нового токена:

```
POST /api/v1/auth/refresh
Authorization: Bearer {{token}}
```

### Мануальное добавление токена

Если понадобится вручную добавить токен в запрос:

1. Откройте нужный запрос
2. Перейдите на вкладку **Authorization**
3. Выберите **Bearer Token**
4. Вставьте токен в поле **Token**

Или добавьте заголовок вручную в табе **Headers**:
```
Authorization: Bearer YOUR_TOKEN_HERE
```

---

## Переменные окружения

Коллекция использует переменные для удобства. Они уже определены в коллекции:

```
base_url      = http://localhost:8081
api_version   = /api/v1
token         = (заполнить после логина)
```

### Как установить/обновить переменные

#### Способ 1: Через Collection Variables

1. Нажмите на коллекцию **StackScout API**
2. Перейдите на вкладку **Variables**
3. Отредактируйте значения:
   - `base_url` — адрес вашего API (по умолчанию `http://localhost:8081`)
   - `api_version` — версия API (по умолчанию `/api/v1`)
   - `token` — оставить пустым, заполнится после логина

#### Способ 2: Через Environment Variables

1. Нажмите на кнопку **Environment** (внизу слева)
2. Нажмите **Create New Environment** или выберите существующее
3. Добавьте переменные:
   ```
   base_url      = http://localhost:8081
   api_version   = /api/v1
   token         = (после логина)
   ```
4. Нажмите **Save**

### Настройка для Production

Если тестируете на production сервере:

```
base_url      = https://api.stackscout.io
api_version   = /api/v1
token         = YOUR_PRODUCTION_TOKEN
```

### Автоматическое заполнение token'а

После запроса **Login** токен автоматически заполнится в переменной `token`. Для этого в запросе **Login** есть скрипт в разделе **Tests**:

```javascript
var jsonData = pm.response.json();
pm.collectionVariables.set("token", jsonData.token);
```

---

## Режимы работы

### Development (локальная разработка)

```
Base URL: http://localhost:8081

Требования:
- Docker контейнеры запущены (PostgreSQL, Redis, RabbitMQ)
- Backend сервис очень работает на порту 8081
```

**Быстро старт:**
```bash
docker-compose up -d postgres redis rabbitmq
cd backend
./gradlew bootRun
```

### Production (на сервере)

```
Base URL: https://api.stackscout.io

Требования:
- Активный JWT токен
- HTTPS соединение
```

Для переключения между окружениями используйте переменные в Postman.

---

## Типичные сценарии использования

### Сценарий 1: Поиск безопасной библиотеки

1. Откройте **Libraries → Search**
2. Измените параметры:
   ```json
   {
     "name": "numpy",
     "minHealthScore": 85,
     "sourceType": "PYPI"
   }
   ```
3. Нажмите **Send**
4. Проверьте результаты и выберите подходящую версию

### Сценарий 2: Мониторинг качества проекта

1. Запустите **Scan Jobs → Start Scan** с параметрами вашего проекта
2. Отслеживайте прогресс через **Scan Jobs → Get Status**
3. Получите результаты через **Scan Jobs → Get Results**

### Сценарий 3: Аналитика лицензий

1. Получите список bibliотек: **Libraries → Get All**
2. Для каждой проверьте лицензию в ответе
3. Используйте **Licenses → Get by ID** для получения классификации

---

## Советы и трюки

### Сохранение истории запросов

Postman автоматически сохраняет историю всех запросов. Откройте **History** слева для быстрого доступа.

### Форматирование JSON

При отправке больших JSON:
1. Используйте вкладку **Body** → **raw** → **JSON**
2. Нажмите Ctrl+Alt+L (или Cmd+Option+L на Mac) для автоформатирования

### Тестирование с разными значениями

Используйте **Collection Runner**:
1. Нажмите **Collection** → **Run**
2. Выберите нужные запросы
3. Нажмите **Run StackScout API**

### Экспорт результатов

После теста нажмите **Export Results** для сохранения отчета.

---

## Решение проблем

### Error 403 Forbidden

**Причина**: Эндпоинт требует JWT токен

**Решение**:
1. Получите токен через `/api/v1/auth/login`
2. Добавьте заголовок: `Authorization: Bearer YOUR_TOKEN`
3. Повторите запрос

### Error 404 Not Found

**Причина**: Неверный URL или ресурс не существует

**Решение**:
1. Проверьте Base URL в переменных окружения
2. Убедитесь что ID ресурса существует
3. Посмотрите доступных ресурсов через GET запрос

### Connection refused

**Причина**: Backend сервис не запущен

**Решение**:
```bash
# Запустите Docker
docker-compose up -d postgres redis rabbitmq

# Запустите backend
cd backend
./gradlew bootRun
```

### Timeout при запросе

**Причина**: Сервер переполнен или медленно отвечает

**Решение**:
1. Увеличьте timeout в Postman: **Settings** → **General** → **Request timeout**
2. Попробуйте с меньшей выборкой (size=5)
3. Проверьте статус базы данных