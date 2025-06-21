FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml .

COPY token-retriever token-retriever

RUN mvn -f token-retriever/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY --from=build /build/token-retriever/target/*.jar ./app.jar

CMD ["java", "-jar", "app.jar"]
