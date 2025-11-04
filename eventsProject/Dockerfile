# Use a lightweight Java image
FROM openjdk:17-jdk-slim

# Create app directory
WORKDIR /app

# Copy the built JAR from target folder
COPY target/eventsProject-1.0.0.jar app.jar

# Expose your app port (adjust if needed)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
