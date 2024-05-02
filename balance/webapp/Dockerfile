FROM openjdk:22 AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean install -DskipTests

FROM openjdk:22
WORKDIR /app
COPY --from=build /app/balance/webapp/target/balance-webapp-0.0.1-SNAPSHOT.jar balance.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "balance.jar"]
