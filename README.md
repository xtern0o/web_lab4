# web_lab4
> SPRINGBOOT URAA


## Запуск

1) склонируйте репозиторий
```bash
git clone https://github.com/xtern0o/web_lab4
cd web_lab4
```

2) сбилдить проект
```bash
cd backend
./gradlew build
cd .. 
```

3) в корне проекта создайте файл `.env` и заполните его нужными данными по подобию `.env.example`
```declarative
DB_NAME         - желаемое имя бд
DB_USERNAME     - желаемый юзернейм в постгресе
DB_PASSWORD     - желаемый пароль от юзера в постгресе
JWT_SECRET      - секрет для jwt (делайте очень длинным)
REDIS_PASSWORD  - пароль от редис базы
```

4) в `frontend/web_lab4_frontend` создайте `.env.production` как `.env.example` ток значениями своими заполнить надо (`VITE_API_URL` ставь `/api` потому что в nginx прокси настроен, а таймаут без разницы там в целом)

5) докер компос!!

   - если нужны логи
    ```bash
    docker-compose up --build
    ```
   - если пох то детечед
   ```bash
   docker-compose up --build -d
   ```
