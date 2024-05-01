FROM openjdk:22

WORKDIR /app

# Copy the Maven project files
COPY . .

# Build the application JAR using Maven Wrapper
RUN ./mvnw install

# Copy the JAR file from the target directory to the current directory
COPY target/appeal-webapp-0.0.1-SNAPSHOT.jar ./appeal.jar

EXPOSE 8080

CMD ["java", "-jar", "appeal.jar"]
