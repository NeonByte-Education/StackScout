# Руководство для контрибьюторов

<div align="center">

**Как внести вклад в развитие StackScout**

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](../LICENSE)

[Процесс разработки](#процесс-разработки) • [Установка](#установка) • [Правила кода](#правила-кода) • [Pull Requests](#pull-requests)

</div>

---

## Для начинающих

### По каким направлениям можно внести вклад?

StackScout активно приветствует контрибьюции в следующих областях:

- **Backend (Java)**: API improvements, performance optimization, bug fixes
- **Frontend (Next.js/React)**: UI/UX improvements, new features, testing
- **DevOps**: Docker, CI/CD, deployment optimization
- **Documentation**: Улучшение документации, добавление примеров
- **Testing**: Unit tests, integration tests, E2E tests
- **Translations**: Локализация на другие языки

### Чего искать для начала?

Начните с issues, отмеченных как:
- `good first issue` - идеальные для новичков
- `help wanted` - нужна помощь сообщества
- `documentation` - работа с документацией

---

## Процесс разработки

### 1. Форк и клонирование репозитория

```bash
# Форк репозитория на GitHub (кнопка Fork)

# Клонирование вашего форка
git clone https://github.com/YOUR_USERNAME/StackScout.git
cd StackScout

# Добавление ссылки на оригинальный репозиторий
git remote add upstream https://github.com/original/StackScout.git
```

### 2. Создание ветки для разработки

```bash
# Получение актуальной версии мастера
git fetch upstream
git checkout upstream/main

# Создание ветки с описательным названием
git checkout -b feature/your-feature-name
# или
git checkout -b fix/issue-description
# или
git checkout -b docs/documentation-update
```

**Правила именования веток:**
- `feature/*` - новые функции
- `fix/*` - исправление ошибок
- `docs/*` - документация
- `refactor/*` - рефакторинг
- `test/*` - добавление тестов

Пример: `feature/license-compatibility-check`

---

## Установка локального окружения

### Требования

- **Java 21+** (для backend)
- **Node.js 18+** (для frontend)
- **PostgreSQL 16**
- **Docker & Docker Compose**
- **Git**

### Backend (Java/Spring Boot)

```bash
cd backend

# Установка зависимостей (gradle wrapper)
./gradlew build

# Запуск в режиме разработки
./gradlew bootRun

# Запуск тестов
./gradlew test

# Запуск тестов с покрытием
./gradlew test jacocoTestReport
```

**Environment переменные:**

```bash
# .env.local или переменные окружения
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/stackscout_dev
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
SPRING_DATA_REDIS_HOST=localhost
SPRING_RABBITMQ_HOST=localhost
```

### Frontend (Next.js/React)

```bash
cd frontend

# Установка зависимостей (используем pnpm)
pnpm install

# Запуск в режиме разработки
pnpm dev

# Сборка для production
pnpm build

# Запуск тестов
pnpm test

# Linting
pnpm lint
```

**Environment переменные:**

```bash
# .env.local
NEXT_PUBLIC_API_URL=http://localhost:8081/api/v1
```

### Docker Compose (быстрый старт)

```bash
# Запуск всех сервисов
docker-compose up -d

# Проверка статуса
docker-compose ps

# Остановка
docker-compose down

# Просмотр логов
docker-compose logs -f app
```

После запуска:
- Backend: http://localhost:8081
- Frontend: http://localhost:3000
- PostgreSQL: localhost:5432
- Redis: localhost:6379
- RabbitMQ: http://localhost:15672 (guest/guest)

---

## Правила кода

### Java (Backend)

#### Стиль кодирования

```java
// ✅ Хорошо: camelCase для переменных/методов
private String packageName;
public PackageDto getPackageById(Long id) { }

// ❌ Недопустимо: snake_case
private String package_name;

// ✅ Используйте описательные имена
public List<PackageDto> findActivePackages() { }

// ❌ Избегайте сокращений
public List<PackageDto> getActPkgs() { }
```

#### Конвенции

```java
// Используйте Spring annotations
@Service
@Transactional
public class PackageService {
    
    @Autowired
    private PackageRepository packageRepository;
    
    // Документация методов
    /**
     * Получить пакет по ID
     *
     * @param id идентификатор пакета
     * @return PackageDto
     * @throws ResourceNotFoundException если пакет не найден
     */
    public PackageDto getPackageById(Long id) {
        // Implementation
    }
    
    // Обработка исключений
    try {
        return packageRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException());
    } catch (DatabaseException e) {
        logger.error("Database error: {}", e.getMessage());
        throw new RuntimeException("Failed to fetch package", e);
    }
}
```

#### Структура класса

```java
@Service
public class ExampleService {
    
    // 1. Константы
    private static final String LOG_PREFIX = "ExampleService";
    
    // 2. Зависимости
    @Autowired
    private ExampleRepository repository;
    
    @Autowired
    private Logger logger;
    
    // 3. Поля
    private String state;
    
    // 4. Конструктор(ы)
    public ExampleService() { }
    
    // 5. Публичные методы
    public void publicMethod() { }
    
    // 6. Приватные методы
    private void privateMethod() { }
}
```

### TypeScript/React (Frontend)

#### Стиль компонентов

```typescript
// ✅ Функциональный компонент с типами
interface PackageCardProps {
  packageId: number;
  name: string;
  healthScore: number;
  onSelect: (id: number) => void;
}

export const PackageCard: React.FC<PackageCardProps> = ({
  packageId,
  name,
  healthScore,
  onSelect,
}) => {
  return (
    <div className="card">
      <h3>{name}</h3>
      <p>Health: {healthScore}</p>
      <button onClick={() => onSelect(packageId)}>Select</button>
    </div>
  );
};

// ❌ Недопустимо: без типов
const PackageCard = (props) => {
  return <div>{props.name}</div>;
};
```

#### Конвенции

```typescript
// Используйте const и let (не var)
const MAX_RETRIES = 3;
let currentAttempt = 0;

// Явная типизация
interface ApiResponse<T> {
  data: T;
  status: number;
  message: string;
}

// Используйте async/await
async function fetchPackages(): Promise<Package[]> {
  try {
    const response = await apiClient.get('/packages');
    return response.data;
  } catch (error) {
    console.error('Failed to fetch packages:', error);
    throw error;
  }
}

// Деструктуризация
const { data, status, message } = response;
```

### Правила во всех языках

1. **Комментарии**: Объясняйте ПОЧЕМУ, а не ЧТО
2. **Документация**: Документируйте публичные методы
3. **Логирование**: Используйте уровни логирования правильно
4. **Производительность**: Профилируйте перед оптимизацией
5. **Безопасность**: Никогда не коммитьте secrets/passwords

---

## Тестирование

### Backend (JUnit 5 + Mockito)

```java
@SpringBootTest
class PackageServiceTest {
    
    @Mock
    private PackageRepository packageRepository;
    
    @InjectMocks
    private PackageService packageService;
    
    @Test
    void shouldReturnPackageById() {
        // Arrange
        Long packageId = 1L;
        Package package = new Package();
        package.setId(packageId);
        package.setName("requests");
        
        when(packageRepository.findById(packageId))
            .thenReturn(Optional.of(package));
        
        // Act
        PackageDto result = packageService.getPackageById(packageId);
        
        // Assert
        assertThat(result.getId()).isEqualTo(packageId);
        assertThat(result.getName()).isEqualTo("requests");
        
        // Verify
        verify(packageRepository, times(1)).findById(packageId);
    }
}
```

### Frontend (Jest)

```typescript
import { render, screen, fireEvent } from '@testing-library/react';
import { PackageCard } from './PackageCard';

describe('PackageCard', () => {
  it('should render package name', () => {
    const mockOnSelect = jest.fn();
    
    render(
      <PackageCard
        packageId={1}
        name="requests"
        healthScore={92}
        onSelect={mockOnSelect}
      />
    );
    
    expect(screen.getByText('requests')).toBeInTheDocument();
  });
  
  it('should call onSelect when clicked', () => {
    const mockOnSelect = jest.fn();
    
    render(
      <PackageCard
        packageId={1}
        name="requests"
        healthScore={92}
        onSelect={mockOnSelect}
      />
    );
    
    fireEvent.click(screen.getByText('Select'));
    expect(mockOnSelect).toHaveBeenCalledWith(1);
  });
});
```

### Требования по тестированию

- **Покрытие**: Минимум 80% для новых функций
- **Unit тесты**: Для всех сервисов и утилит
- **Integration тесты**: Для API endpoints
- **Test naming**: Описывает что тестируется и ожидаемый результат

Запуск:
```bash
# Backend
./gradlew test

# Frontend
pnpm test
```

---

## Pull Requests

### Прежде чем создавать PR

1. **Обновите вашу ветку**
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

2. **Запустите тесты локально**
   ```bash
   # Backend
   ./gradlew test
   
   # Frontend
   pnpm test
   ```

3. **Проверьте лиминг**
   ```bash
   # Backend - через IDE или
   ./gradlew checkstyleMain
   
   # Frontend
   pnpm lint
   ```

4. **Создайте нужные тесты**

### Создание PR

1. **Пуш ветки в ваш форк**
   ```bash
   git push origin feature/your-feature-name
   ```

2. **Откройте PR на GitHub**

3. **Заполните описание PR по шаблону:**

```markdown
## Описание
Что было сделано и почему

## Type of change
- [ ] Bug fix (исправление ошибки)
- [ ] New feature (новая функция)
- [ ] Breaking change
- [ ] Documentation update

## Как это было протестировано?
Описание процесса тестирования

## Чек-лист
- [ ] Код следует стилю проекта
- [ ] Добавлены необходимые тесты
- [ ] Обновлена документация
- [ ] Все тесты проходят
- [ ] Не добавлены ненужные файлы

## Скриншоты (если UI изменения)
[Добавить скриншоты]

## Закрывает issue (если есть)
Closes #123
```

### Требования к PR

- [ ] Один функционал на один PR
- [ ] Понятное описание изменений
- [ ] Тесты для новых функций
- [ ] Обновленная документация
- [ ] Без конфликтов с `main` веткой

### Что происходит после создания PR?

1. **CI/CD проверки**: Автоматизированное тестирование и linting
2. **Код Review**: Минимум 1 одобрения от мейнтейнера
3. **Обсуждение**: Если есть замечания, их нужно исправить
4. **Merge**: Мейнтейнер объединит ветку в `main`

---

## Commit сообщения

Используйте **Conventional Commits**:

```
<type>(<scope>): <subject>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```

### Примеры

```
feat(api): add package search endpoint
- Implement search by name and health score
- Add pagination support
- Tests included

Closes #123

---

fix(frontend): resolve package card rendering bug
The component was not updating when props changed

---

docs(architecture): update database schema diagram

---

refactor(service): simplify health score calculation
No functional changes, improved readability

---

test(backend): add unit tests for PackageService
```

### Type (тип коммита)

| Type | Описание |
|------|---------|
| `feat` | Новая функция |
| `fix` | Исправление ошибки |
| `docs` | Документация |
| `style` | Форматирование (не меняет логику) |
| `refactor` | Рефакторинг кода |
| `perf` | Оптимизация производительности |
| `test` | Добавление тестов |
| `chore` | Изменение конфигурации, зависимостей |

### Scope (область)

Можно опустить, но рекомендуется указывать:
- `api`, `service`, `controller`, `repository` (backend)
- `component`, `page`, `lib`, `hook` (frontend)
- `docker`, `pipeline`, `config` (devops)

---

## Общение с сообществом

### Где задавать вопросы?

- **Issues** на GitHub - для баг-репортов и функций
- **Discussions** на GitHub - для вопросов и идей
- **Pull Requests** - для обсуждения кода

### Правила вежливости (Code of Conduct)

- Будьте уважительны к другим контрибьюторам
- Принимайте конструктивную критику
- Сосредоточьтесь на проблеме, а не на человеке
- Не допускать дискриминацию и оскорбления

---

## Раскрытие и признание

Мы благодарны всем, кто вносит вклад в StackScout!

Список контрибьюторов ведется в [CONTRIBUTORS.md](#) (в разработке).

---

## Дополнительные ресурсы

- [Architecture](./ARCHITECTURE.md) - Техническая архитектура
- [API Documentation](./API.md) - REST API
- [Database Schema](./DATABASE.md) - Структура БД
- [Backend README](../backend/README.md)
- [Frontend README](../frontend/README.md)
- [License](../LICENSE)

---

## Вопросы?

Открывайте issue с меткой `question` или создавайте Discussion.

**Спасибо за вклад в StackScout! 🚀**
