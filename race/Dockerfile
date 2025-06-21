FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml .

COPY race race

RUN mvn -f race/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY --from=build /build/race/target/*.jar ./app.jar

CMD ["java", "-jar", "app.jar"]
