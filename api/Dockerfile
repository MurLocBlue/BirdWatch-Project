FROM maven:3.8.4-openjdk-11 as builder

WORKDIR /api
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:11-slim
WORKDIR /api
COPY --from=builder /api/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"] 