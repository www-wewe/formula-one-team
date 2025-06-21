FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml .

COPY component component



RUN mvn -f component/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY --from=build /build/component/target/*.jar ./app.jar

CMD ["java", "-jar", "app.jar"]
