FROM openjdk:22 AS build
WORKDIR /app
RUN rm -rf /app/*
COPY . .
RUN ./mvnw clean install -DskipTests

FROM openjdk:22
WORKDIR /app
COPY --from=build /app/merchant/webapp/target/merchant-webapp-0.0.1-SNAPSHOT.jar merchant.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "merchant.jar"]
