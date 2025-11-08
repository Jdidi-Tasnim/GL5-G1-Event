# Base image: Eclipse Temurin JDK 17
FROM eclipse-temurin:17-jre-alpine

# Labels
LABEL maintainer="jdiditasnim"
LABEL version="1.0"
LABEL description="Events Project Spring Boot Application (H2)"

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring

# Working directory
WORKDIR /app

# Copy jar from target folder
COPY --chown=spring:spring target/eventsProject-1.0.0-SNAPSHOT.jar app.jar

# Set non-root user
USER spring:spring

# Expose port
EXPOSE 8080

# Set environment for H2
ENV SPRING_PROFILES_ACTIVE=h2
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run Spring Boot app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

