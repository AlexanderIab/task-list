FROM openjdk:17-jdk-slim
COPY build/libs/*.jar application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]