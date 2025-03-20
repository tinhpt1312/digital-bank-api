FROM openjdk:17-jdk-alpine
LABEL authors="Tinh"

ENV APP_HOME=/app
WORKDIR ${APP_HOME}

COPY target/digital-bank-api-0.0.1-SNAPSHOT.jar ${APP_HOME}/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]