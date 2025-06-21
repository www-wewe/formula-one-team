FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml .

COPY car car

RUN mvn -f car/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY --from=build /build/car/target/*.jar ./app.jar

CMD ["java", "-jar", "app.jar"]
