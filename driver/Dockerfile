FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml .

COPY driver driver



RUN mvn -f driver/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY --from=build /build/driver/target/*.jar ./app.jar

CMD ["java", "-jar", "app.jar"]
