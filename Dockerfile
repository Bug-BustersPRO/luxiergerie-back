# Use a base image with Java 21
FROM openjdk:21

# Copy the JAR package into the image
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Copy the import.sql file into the image (assuming it exists at the specified path)
COPY ./src/main/resources/import.sql /docker-entrypoint-initdb.d/

# Ensure the correct permissions on the import.sql file
RUN chmod 644 /docker-entrypoint-initdb.d/import.sql

# Expose the application port
EXPOSE 8090

# Expose the MySQL port (if your application needs to connect to MySQL within the same container)
EXPOSE 3306

# Run the App
ENTRYPOINT ["java", "-jar", "/app.jar"]