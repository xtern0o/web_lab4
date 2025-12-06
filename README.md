# web_lab4
> SPRINGBOOT URAA

## Первый запуск

### Предварительная сборка
- склонируйте репозиторий
```bash
git clone https://github.com/xtern0o/web_lab4
cd web_lab4
```
- сбилдите проект
```bash
cd backend
./gradlew build
cd .. 
```

- в корне проекта создайте файл `.env` и заполните его нужными данными по подобию `.env.example`
   - **Важно**: Данные с перфиксом `KEYCLOAK_` желательно заполнить после настройки своего реалма и клиента Keycloak.

- в `frontend/web_lab4_frontend` создайте `.env.production` как `.env.example` ток значениями своими заполнить надо (`VITE_API_URL` ставь `/api` потому что в nginx прокси настроен, а таймаут без разницы там в целом)

### Настройка Keycloak

- Запустите контейнеры `keycloak_postgres` (том для хранения данных конфигурации КК) и `keycloak` для настройки сервера авторизации
```bash
docker-compose up -d --build postgres keycloak
```

- По адресу *localhost:${KEYCLOAK_PORT}* зайдите в панель управления кейклоком авторизуйтесь по данным из `.env`

- Создайте realm и client. Эти данные внесите в соответствующие поля `.env`
  - (потом подробнее распишу, _наверное_...)
  - Client должен соответствовать стандарту **OAuth 2.0** (конкретно **OpenID**), реализовывать **Authentication Code Flow (Standard Flow)**
  - То есть, при создании клиента выбрать: `Client authentication: ON`; `Standard Flow`; `Direct Access Grant` (для role-менеджмента)
  - С урлами отдельная песня.
      - Valid Redirect URL: `http://<frontend_host:frontend_port>/callback` - то, где фронт обрабатывает Authentication Code; `http://localhost:5173/callback` - очень удобно для дев-режима (убрать при деплое, желательно)
      - Web Origins: `http://<backend_host:backend_port>` - для того, чтобы можно было с нашего бека отправлять запросы в кк.
      - Остальное можно оставить пустым
- После создания клиента запишите в `.env` `KEYCLOAK_CLIENT_SECRET` (`clients > "client name" > credentials > client secret`)

### Запуск основной части проекта

- Запустите backend, frontend и postgres
```bash
docker-compose up --build backend frontend postgres
```

- Наслаждайтесь

## Повторный запуск

При повторном запуске спокойно запускаем через `docker-compose up -d --build`, так как весь конфиг кк сохранен в томе

