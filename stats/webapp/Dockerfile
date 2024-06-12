FROM openjdk:22 AS build
WORKDIR /app
RUN rm -rf /app/*
COPY . .
RUN ./mvnw clean install -DskipTests

FROM openjdk:22
WORKDIR /app
COPY --from=build /app/stats/webapp/target/stats-webapp-0.0.1-SNAPSHOT.jar stats.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "stats.jar"]
