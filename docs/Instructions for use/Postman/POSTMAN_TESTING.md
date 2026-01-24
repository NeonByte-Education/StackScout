# 🧪 Руководство по тестированию API через Postman

## 📋 Содержание
- [Запуск приложения](#запуск-приложения)
- [Импорт коллекции в Postman](#импорт-коллекции-в-postman)
- [Доступные эндпоинты](#доступные-эндпоинты)
- [Примеры запросов](#примеры-запросов)

---

## 🚀 Запуск приложения

### Режим разработки (H2 база данных)
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```
Приложение будет доступно на: **http://localhost:8081**

### Production режим (PostgreSQL)
```bash
docker-compose up -d
./gradlew bootRun
```
Приложение будет доступно на: **http://localhost:8080**

---

## 📥 Импорт коллекции в Postman

1. Откройте Postman
2. Нажмите **Import** (левый верхний угол)
3. Выберите файл `postman-collection.json` из корня проекта
4. Коллекция "StackScout API" появится в вашем списке

---

## 🎯 Доступные эндпоинты

### 🏥 Health & Status

#### 1. Health Check
```http
GET http://localhost:8081/api/v1/health
```
**Ответ:**
```json
{
  "status": "UP",
  "service": "StackScout API",
  "version": "1.0.0",
  "timestamp": "2026-01-24T12:00:00",
  "environment": "development"
}
```

#### 2. Ping
```http
GET http://localhost:8081/api/v1/ping
```
**Ответ:**
```json
{
  "message": "pong",
  "timestamp": "2026-01-24T12:00:00"
}
```

---

### 📚 Libraries

#### 1. Получить все библиотеки (с пагинацией)
```http
GET http://localhost:8081/api/v1/libraries?page=0&size=10
```
**Ответ:**
```json
{
  "libraries": [...],
  "totalElements": 3,
  "currentPage": 0,
  "pageSize": 10,
  "totalPages": 1
}
```

#### 2. Получить библиотеку по ID
```http
GET http://localhost:8081/api/v1/libraries/1
```
**Ответ:**
```json
{
  "id": 1,
  "name": "requests",
  "version": "2.31.0",
  "source": "pypi",
  "license": "Apache-2.0",
  "healthScore": 95,
  "lastRelease": "2023-05-22",
  "repository": "https://github.com/psf/requests",
  "description": "HTTP библиотека для Python"
}
```

#### 3. Поиск библиотек
```http
GET http://localhost:8081/api/v1/libraries/search?query=react&source=npm
```
**Параметры:**
- `query` (optional) - текст для поиска
- `source` (optional) - источник: pypi, npm, dockerhub

#### 4. Статистика библиотек
```http
GET http://localhost:8081/api/v1/libraries/stats
```
**Ответ:**
```json
{
  "totalLibraries": 3,
  "sources": {
    "pypi": 2,
    "npm": 1,
    "dockerhub": 0
  },
  "averageHealthScore": 97.33
}
```

#### 5. Создать библиотеку
```http
POST http://localhost:8081/api/v1/libraries
Content-Type: application/json

{
  "name": "express",
  "version": "4.18.2",
  "source": "npm",
  "license": "MIT",
  "healthScore": 97,
  "lastRelease": "2023-10-09",
  "repository": "https://github.com/expressjs/express",
  "description": "Fast, unopinionated, minimalist web framework"
}
```
**Ответ:**
```json
{
  "message": "Библиотека успешно создана",
  "library": {
    "id": 4,
    "name": "express",
    ...
  }
}
```

#### 6. Обновить библиотеку
```http
PUT http://localhost:8081/api/v1/libraries/4
Content-Type: application/json

{
  "version": "4.19.0",
  "healthScore": 98,
  "lastRelease": "2024-01-15"
}
```
**Ответ:**
```json
{
  "message": "Библиотека успешно обновлена",
  "library": {
    "id": 4,
    "name": "express",
    "version": "4.19.0",
    ...
  }
}
```

#### 7. Удалить библиотеку
```http
DELETE http://localhost:8081/api/v1/libraries/4
```
**Ответ:**
```json
{
  "message": "Библиотека успешно удалена",
  "id": "4"
}
```

---

### 🔍 Collector

#### 1. Запустить сканирование
```http
POST http://localhost:8081/api/v1/collector/scan
Content-Type: application/json

{
  "source": "pypi",
  "packages": ["requests", "django", "flask"]
}
```
**Ответ:**
```json
{
  "message": "Сканирование запущено",
  "source": "pypi",
  "packages": ["requests", "django", "flask"],
  "status": "processing",
  "timestamp": "2026-01-24T12:00:00"
}
```

#### 2. Статус коллектора
```http
GET http://localhost:8081/api/v1/collector/status
```
**Ответ:**
```json
{
  "collectorStatus": "active",
  "lastScan": "2026-01-24T10:00:00",
  "totalScans": 42,
  "queueSize": 5
}
```

---

## 🧪 Примеры запросов

### Сценарий 1: Полный цикл работы с библиотекой

1. **Проверьте, что API работает**
   ```
   GET /api/v1/health
   ```

2. **Посмотрите все библиотеки**
   ```
   GET /api/v1/libraries?page=0&size=10
   ```

3. **Создайте новую библиотеку**
   ```
   POST /api/v1/libraries
   {
     "name": "fastapi",
     "version": "0.109.0",
     "source": "pypi",
     "license": "MIT",
     "healthScore": 99,
     "lastRelease": "2024-01-10",
     "repository": "https://github.com/tiangolo/fastapi",
     "description": "FastAPI framework"
   }
   ```

4. **Получите созданную библиотеку по ID** (используйте ID из ответа)
   ```
   GET /api/v1/libraries/4
   ```

5. **Обновите библиотеку**
   ```
   PUT /api/v1/libraries/4
   {
     "version": "0.110.0",
     "healthScore": 100
   }
   ```

6. **Удалите библиотеку**
   ```
   DELETE /api/v1/libraries/4
   ```

### Сценарий 2: Поиск и фильтрация

1. **Найдите все библиотеки из PyPI**
   ```
   GET /api/v1/libraries/search?source=pypi
   ```

2. **Найдите библиотеки по имени**
   ```
   GET /api/v1/libraries/search?query=django
   ```

3. **Комбинированный поиск**
   ```
   GET /api/v1/libraries/search?query=react&source=npm
   ```

4. **Посмотрите статистику**
   ```
   GET /api/v1/libraries/stats
   ```

### Сценарий 3: Запуск сканирования

1. **Проверьте статус коллектора**
   ```
   GET /api/v1/collector/status
   ```

2. **Запустите сканирование PyPI пакетов**
   ```
   POST /api/v1/collector/scan
   {
     "source": "pypi",
     "packages": ["numpy", "pandas", "scikit-learn"]
   }
   ```

3. **Запустите сканирование npm пакетов**
   ```
   POST /api/v1/collector/scan
   {
     "source": "npm",
     "packages": ["react", "vue", "angular"]
   }
   ```

---

## 🐛 Обработка ошибок

### Ошибка валидации (400 Bad Request)
```json
{
  "message": "Имя библиотеки обязательно",
  "timestamp": "2026-01-24T12:00:00"
}
```

### Ресурс не найден (404 Not Found)
```json
{
  "message": "Библиотека с ID 999 не найдена",
  "timestamp": "2026-01-24T12:00:00"
}
```

### Внутренняя ошибка сервера (500 Internal Server Error)
```json
{
  "message": "Ошибка при создании библиотеки: ...",
  "timestamp": "2026-01-24T12:00:00"
}
```

---

## 📝 Заметки

- **Порт по умолчанию:** 8081 (dev режим) или 8080 (prod режим)
- **CORS:** Разрешены запросы из любых источников
- **Формат дат:** ISO 8601 (например: "2024-01-15")
- **ID библиотек:** Автоинкремент, начиная с 1

---

## 🔗 Полезные ссылки

- Swagger UI: http://localhost:8081/swagger-ui.html
- H2 Console: http://localhost:8081/h2-console
- Actuator Health: http://localhost:8081/actuator/health
- Actuator Metrics: http://localhost:8081/actuator/prometheus

---

## ✅ Чеклист для тестирования

- [ ] Health check работает
- [ ] Получение всех библиотек с пагинацией
- [ ] Получение библиотеки по ID
- [ ] Поиск библиотек по query и source
- [ ] Статистика библиотек
- [ ] Создание новой библиотеки
- [ ] Обновление библиотеки
- [ ] Удаление библиотеки
- [ ] Запуск сканирования
- [ ] Получение статуса коллектора
- [ ] Обработка ошибок (404, 400, 500)

---

**Готово! Теперь вы можете полноценно тестировать API через Postman** 🚀
