# web_lab4
> SPRINGBOOT URAA


## Предварительная сборка
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

## Настройка Keycloak

- Запустите контейнеры `postgres` и `keycloak` для настройки сервера авторизации
```bash
docker-compose up -d --build postgres keycloak
```

- По адресу *localhost:${KEYCLOAK_PORT}* зайдите в панель управления кейклоком авторизуйтесь по данным из `.env`

- Создайте realm и client. Эти данные внесите в соответствующие поля `.env`
  - (потом подробнее распишу, _наверное_...)
  - Realm должен соответствовать стандарту **OAuth 2.0** (конкретно **OpenID**), реализовывать **Authentication Code Flow (Standard Flow)**
  - Для клиента внимательно надо указать все урлы для редиректа!!

- Также запишите в `.env` `KEYCLOAK_CLIENT_SECRET` (`clients > "client name" > credentials > client secret`)

## Запуск основной части проекта

- Запустите backend и frontend
```bash
docker-compose up --build backend frontend 
```

- Наслаждайтесь
