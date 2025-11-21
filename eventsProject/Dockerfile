# Use a lightweight Java image
FROM eclipse-temurin:17-jre-alpine


# Create app directory
WORKDIR /app

# Copy the built JAR from target folder
COPY target/eventsProject-1.0.0-SNAPSHOT.jar app.jar

# Expose your app port (adjust if needed)
EXPOSE 80

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
