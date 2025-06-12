FROM openjdk:21-jdk
LABEL authors="Allan"
# Répertoire de travail dans le conteneur
WORKDIR /app

# Copier les .jar nécessaires
COPY domain/target/domain-0.0.1-SNAPSHOT.jar ./libs/domain.jar
COPY api/target/api-0.0.1-SNAPSHOT.jar ./libs/api.jar
COPY infrastructure/target/infrastructure-0.0.1-SNAPSHOT.jar ./libs/infrastructure.jar
COPY app/target/app-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080

# Commande pour exécuter le .jar principal
CMD ["java", "-jar", "app.jar"]



