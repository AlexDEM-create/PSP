FROM openjdk:22 AS build
WORKDIR /app
RUN rm -rf /app/*
COPY . .
RUN ./mvnw clean install -DskipTests

FROM openjdk:22
WORKDIR /app
COPY --from=build /app/payment-method/webapp/target/payment-method-webapp-0.0.1-SNAPSHOT.jar payment-method.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "payment-method.jar"]
