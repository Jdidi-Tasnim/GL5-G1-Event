# Utiliser une image Java
FROM openjdk:17-jdk-slim

# Créer un dossier de travail
WORKDIR /app

# Copier le JAR généré par Maven
COPY target/eventsProject-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port de ton application
EXPOSE 8089

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
