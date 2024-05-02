FROM openjdk:22 AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean install -DskipTests

FROM openjdk:22
WORKDIR /app
COPY --from=build /app/terminal/webapp/target/terminal-webapp-0.0.1-SNAPSHOT.jar terminal.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "terminal.jar"]
