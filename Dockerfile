FROM openjdk:17-jdk-alpine
LABEL authors="Стахнов"
WORKDIR /app
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]