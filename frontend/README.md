<div align="center">
 
# StackScout - Frontend
 
**Пользовательский интерфейс платформы анализа Open Source библиотек**
 
[![Next.js](https://img.shields.io/badge/Next.js-15-black?logo=nextdotjs)](https://nextjs.org/)
[![React](https://img.shields.io/badge/React-19-61DAFB?logo=react)](https://react.dev/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind%20CSS-4.0-38B2AC?logo=tailwindcss)](https://tailwindcss.com/)
 
</div>
 
---
 
## Описание
 
Frontend часть StackScout предоставляет интуитивно понятный и современный интерфейс для визуализации данных об Open Source библиотеках, мониторинга их "здоровья" и управления рисками лицензирования.
 
## Возможности (в разработке)
 
- **Интерактивные дашборды**: Визуализация аналитики и метрик здоровья.
- **Поиск и фильтрация**: Удобный поиск библиотек по репозиториям PyPI и Docker Hub.
- **Управление проектами**: Возможность отслеживать зависимости своих проектов.
- **Адаптивный дизайн**: Корректное отображение на всех типах устройств.
 
## Технологический стек
 
- **Фреймворк**: Next.js 15 (App Router)
- **Библиотека**: React 19
- **Стилизация**: Tailwind CSS 4
- **Язык**: TypeScript
- **Состояние**: TanStack Query (React Query)
 
---
 
## Быстрый старт
 
### Требования
- Node.js 18.x или выше
- npm или yarn
 
### Установка зависимостей
 
```bash
npm install
```
 
### Запуск в режиме разработки
 
```bash
npm run dev
```
Фронтенд будет доступен по адресу: **http://localhost:3000**
 
### Связь с API
Для настройки подключения к бэкенду используйте файл `.env.local`:
```env
NEXT_PUBLIC_API_URL=http://localhost:8081/api/v1
```