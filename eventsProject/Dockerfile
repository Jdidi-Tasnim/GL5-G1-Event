# Multi-stage build pour optimiser la taille de l'image
FROM eclipse-temurin:17-jdk-alpine as builder

WORKDIR /app

# Copier le fichier JAR généré
COPY target/eventsProject-*.jar app.jar

# Extraire les layers pour optimiser le cache Docker
RUN java -Djarmode=layertools -jar app.jar extract

# Stage final - image légère
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Créer un utilisateur non-root pour la sécurité
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copier les layers extraits
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./

# Exposer le port de l'application
EXPOSE 8080

# Démarrer l'application
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
