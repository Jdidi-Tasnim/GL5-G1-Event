# Use Eclipse Temurin (Official OpenJDK builds) as base image
FROM eclipse-temurin:17-jre-alpine

# Add labels for better image management
LABEL maintainer="jdiditasnim"
LABEL version="1.0"
LABEL description="Events Project Application"

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Set working directory
WORKDIR /app

# Copy the JAR file from target folder
COPY --chown=spring:spring target/eventsProject-1.0.0-SNAPSHOT.jar app.jar

# Switch to non-root user
USER spring:spring

# Expose port 8080
EXPOSE 8080

# Set Java options
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
