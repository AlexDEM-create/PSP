FROM openjdk:22 AS build
WORKDIR /app
RUN rm -rf /app/*
COPY . .
RUN ./mvnw clean install -DskipTests

FROM openjdk:22
WORKDIR /app
COPY --from=build /app/appeal/webapp/target/appeal-webapp-0.0.1-SNAPSHOT.jar appeal.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "appeal.jar"]
