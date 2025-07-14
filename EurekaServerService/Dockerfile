FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE ${PORT:-9099}

ENTRYPOINT ["java", "-Dserver.port=${PORT:-9099}", "-jar", "app.jar"]