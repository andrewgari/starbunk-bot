FROM openjdk:17-alpine
COPY build/libs/*.jar app.jar
COPY build/resources/main/application.properties .
ENTRYPOINT ["java", "-jar", "/app.jar"]