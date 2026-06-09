# syntax=docker/dockerfile:1.7

FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace/app

COPY app/pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -B -ntp dependency:go-offline

COPY app/src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -ntp clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

ENV SERVER_PORT=8080
ENV JAVA_OPTS=""

COPY --from=build /workspace/app/target/app-0.0.1-SNAPSHOT.jar /app/app.jar

USER spring:spring

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
