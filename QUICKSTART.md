# 🚀 Быстрый старт StackScout

## Запуск за 2 минуты

### Требования
- Java 21+
- Git

### Шаги

1. **Склонируйте и запустите:**
   ```bash
   git clone https://github.com/NeonByte-Education/StackScout.git
   cd StackScout
   ./gradlew bootRun
   ```

2. **Откройте в браузере:**
   - API: http://localhost:8081
   - Swagger: http://localhost:8081/swagger-ui.html
   - H2 Console: http://localhost:8081/h2-console

Вот и всё! Приложение работает с встроенной базой данных H2.

---

## Режимы работы

### 🛠️ Dev режим (по умолчанию)
Работает без Docker, без настройки БД
```bash
./gradlew bootRun
# или
./gradlew bootRun --args='--spring.profiles.active=dev'
```

**Особенности:**
- ✅ H2 база данных в памяти
- ✅ Работает сразу после клонирования
- ✅ Не требует Docker
- ✅ H2 Console для просмотра данных
- ⚠️ Данные не сохраняются после перезапуска

### 🚀 Production режим
Полное окружение с PostgreSQL, Redis, RabbitMQ

```bash
# 1. Запустите Docker контейнеры
docker-compose up -d

# 2. Запустите приложение
./gradlew bootRun
```

**Особенности:**
- ✅ PostgreSQL база данных
- ✅ Redis кэширование
- ✅ RabbitMQ очереди
- ✅ Данные сохраняются
- ✅ Prometheus + Grafana мониторинг

---

## 🔐 Доступ к сервисам

### Локальная разработка (dev)
| Сервис | URL | Логин | Пароль |
|--------|-----|-------|--------|
| API | http://localhost:8081 | - | - |
| Swagger UI | http://localhost:8081/swagger-ui.html | - | - |
| H2 Console | http://localhost:8081/h2-console | `sa` | (пусто) |
| Actuator | http://localhost:8081/actuator | - | - |

**H2 Console настройки:**
- JDBC URL: `jdbc:h2:mem:stackscout`
- Username: `sa`
- Password: (оставить пустым)

### Production (Docker)
| Сервис | URL | Логин | Пароль |
|--------|-----|-------|--------|
| API | http://localhost:8081 | - | - |
| PostgreSQL | localhost:5432 | `postgres` | `postgres` |
| Redis | localhost:6379 | - | - |
| RabbitMQ | http://localhost:15672 | `guest` | `guest` |

---

## 🛠️ Полезные команды

```bash
# Сборка проекта
./gradlew build

# Запуск тестов
./gradlew test

# Очистка
./gradlew clean

# Пересборка
./gradlew clean build

# Создание JAR
./gradlew bootJar
# Результат: build/libs/StackScout-1.0.0-SNAPSHOT.jar

# Запуск JAR
java -jar build/libs/StackScout-1.0.0-SNAPSHOT.jar
```

---

## 📝 Первые шаги

После запуска попробуйте:

1. **Swagger UI** (http://localhost:8081/swagger-ui.html)
   - Интерактивная документация API
   - Тестирование endpoint'ов

2. **H2 Console** (http://localhost:8081/h2-console)
   - Просмотр структуры БД
   - Выполнение SQL запросов

3. **Health Check**
   ```bash
   curl http://localhost:8081/actuator/health
   ```

---

## ❓ Проблемы?

### Ошибка: "Address already in use"
Порт 8081 занят. Измените в `application.yml`:
```yaml
server:
  port: 8082
```

### Ошибка с Java версией
Проверьте версию Java:
```bash
java -version
# Должна быть 21+
```

### Docker контейнеры не запускаются
Проверьте, что Docker запущен:
```bash
docker ps
```

---

## 📚 Дополнительная информация

- [Полная документация](README.md)
- [API документация](docs/API.md)
- [Архитектура](docs/ARCHITECTURE.md)
- [Contributing](docs/CONTRIBUTING.md)
