FROM openjdk:22 AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean install -DskipTests

FROM openjdk:22
WORKDIR /app
COPY --from=build /app/trader-team/webapp/target/trader-team-webapp-0.0.1-SNAPSHOT.jar trader-team.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "trader-team.jar"]
