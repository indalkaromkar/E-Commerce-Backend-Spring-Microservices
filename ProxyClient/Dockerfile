FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 9056

ENTRYPOINT ["java", "-jar", "app.jar"]