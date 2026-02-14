## Launch instructions

### Launch for local dev

```
docker-compose up --build database
./gradlew bootDev
```

### Launch in docker

```
./gradlew bootJar
docker-compose up --build
```

## Also pls read docs/Doc.md