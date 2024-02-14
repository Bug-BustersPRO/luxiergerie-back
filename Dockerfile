# Use an OpenJDK Runtime as a parent image
FROM openjdk:17-jre-alpine
# Define environment variables
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \JAVA_OPTS=""
# Set the working directory to /app
WORKDIR /app
# Copy the executable into the container at /app
ADD target/*.jar app.jar
# Make port 8080 available to the world outside this container
EXPOSE 8080
# Run app.jar when the container launches
CMD ["java", "-jar", "/app/app.jar"]
