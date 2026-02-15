## Launch instructions

### Environment Configuration

Root .env file (project root)
```
POSTGRES_DB=postgres
POSTGRES_USER=postgres
POSTGRES_PASSWORD=password
DB_HOST=database
DB_PORT=5432

JWT_ACCESS_SECRET=78e7164fb90a598d3bd4caca2ef8568f0710283a4b80cca0c31afa40526c44fe
JWT_REFRESH_SECRET=769b7243a7d91d3b456eb0af013741b78b55af6fbc2266916b190d17ec9820e4
JWT_ACCESS_EXP_TIME=300
JWT_REFRESH_EXP_TIME=172000

CARD_ENCRYPTION_KEY=sdWbynRKwRL3pGRSm9vSoZo8JiuXBmJh

CORS_ALLOWED_ORIGINS=127.0.0.1
CORS_ALLOWED_METHODS=GET,POST,PATCH,DELETE,OPTIONS
CORS_ALLOWED_HEADERS=Keep-Alive,User-Agent,X-Requested-With,Content-Type
CORS_ALLOW_CREDENTIALS=true

ADMIN_PASSWORD=password
```
Database .env file (database folder)
```
POSTGRES_DB=postgres
POSTGRES_USER=postgres
POSTGRES_PASSWORD=password
DB_PORT=5432
```

### Launch for local dev

```
# Start only database
docker-compose up --build database -d
# Run application
./gradlew bootRun
```

### Launch in docker

```
./gradlew bootJar
docker-compose up --build
```

### After startup, visit http://localhost:8080/swagger-ui.html

## Also pls read docs/Doc.md