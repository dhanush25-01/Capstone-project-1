# Build an executable Spring Boot JAR with Java 17
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B clean package -DskipTests

# Run the application on Render with Java 17
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/DhanushMart-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 10000
ENTRYPOINT ["java", "-jar", "app.jar"]
