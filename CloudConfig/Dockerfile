FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE ${PORT:-9296}

ENTRYPOINT ["java", "-Dserver.port=${PORT:-9296}", "-jar", "app.jar"]