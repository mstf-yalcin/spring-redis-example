#FROM openjdk:17-slim as build
#WORKDIR /app
#
#COPY target/*.jar app.jar
#ENTRYPOINT ["java","-jar","app.jar"]

#MultiStage
FROM openjdk:17-slim as build
WORKDIR /app
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw package

FROM eclipse-temurin:17-jre-alpine AS jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
#ENTRYPOINT ["java","-Dspring.profiles.active=production","-jar","app.jar"]



#MultiStage2 Clean install
#RUN ./mvnw install -DskipTests
#FROM openjdk:17-slim as build
#WORKDIR /app
#COPY . .
#RUN ./mvnw clean install -U
#
#FROM eclipse-temurin:17-jre-alpine AS jre
#WORKDIR /app
#COPY --from=build app/target/*.jar app.jar
#ENTRYPOINT ["java","-jar","app.jar"]