# Стадия сборки
FROM node:20-alpine AS builder

WORKDIR /app

# Копируем файлы зависимостей
COPY package.json pnpm-lock.yaml ./

# Устанавливаем зависимости
RUN npm install -g pnpm && pnpm install --frozen-lockfile

# Копируем исходный код
COPY . .

# Собираем приложение
RUN pnpm build

# Стадия production
FROM node:20-alpine

WORKDIR /app

# Устанавливаем pnpm
RUN npm install -g pnpm

# Копируем файлы зависимостей
COPY package.json pnpm-lock.yaml ./

# Устанавливаем производственные зависимости
RUN pnpm install --frozen-lockfile --prod

# Копируем собранное приложение из builder
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/public ./public

# Expose порт
EXPOSE 3000

# Стартуем приложение
CMD ["pnpm", "start"]
