FROM openjdk:22 AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean install -DskipTests

FROM openjdk:22
WORKDIR /app
COPY --from=build /app/card/webapp/target/card-webapp-0.0.1-SNAPSHOT.jar card.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "card.jar"]
