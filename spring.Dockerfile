FROM eclipse-temurin:21-alpine

WORKDIR /app

COPY /target/tcrm-1.0.0.jar ./app.jar

RUN apk add --no-cache curl

EXPOSE 8080

ENTRYPOINT ["java", "-Xms256m", "-Xmx2048m", "-jar", "./app.jar"]
CMD ["--spring.profiles.active=prod"]