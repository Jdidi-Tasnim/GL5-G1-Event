# Utiliser une image Java (version corrigée pour compatibilité)
FROM eclipse-temurin:11-jre

# Créer un dossier de travail
WORKDIR /app

# CORRECTION : Utiliser le bon nom de JAR (0.0.3-RELEASE au lieu de 0.0.1-SNAPSHOT)
COPY target/eventsProject-0.0.3-RELEASE.jar app.jar

# Exposer le port de l'application (8080 au lieu de 8089 pour cohérence)
EXPOSE 8080

# Lancer l'application (chemin absolu corrigé)
ENTRYPOINT ["java", "-jar", "/app/app.jar"]