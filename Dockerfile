FROM gradle:8.7-jdk17 AS build
WORKDIR /app
COPY src /app/src
COPY build.gradle /app/
RUN gradle clean bootJar

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]