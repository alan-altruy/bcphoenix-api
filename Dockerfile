# ==========================================
# 1. BUILD : Compilation Maven
# ==========================================
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copier les fichiers Maven en premier pour profiter du cache Docker
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN chmod +x mvnw

# Télécharger les dépendances
RUN ./mvnw dependency:go-offline -B

# Copier le code source
COPY src ./src

# Compiler l'application
RUN ./mvnw clean package -DskipTests -B

# ==========================================
# 2. RUNTIME : Exécution Spring Boot
# ==========================================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Installer curl pour les healthchecks Docker
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

# Copier uniquement le JAR compilé
COPY --from=build /app/target/*.jar app.jar

# Utilisateur non-root
RUN useradd -r -u 10001 spring \
    && chown -R spring:spring /app

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]