# ---- Stufe 1: Anwendung bauen ----
FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /workspace

# Zuerst nur Maven-Wrapper und pom.xml: Diese Schichten bleiben im Cache,
# solange sich die Abhaengigkeiten nicht aendern.
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN ./mvnw -B -q dependency:go-offline

# Dann den Quellcode. Tests und Linter laufen vorher in der CI-Pipeline.
COPY src src
RUN ./mvnw -B -q package -DskipTests -Dcheckstyle.skip

# ---- Stufe 2: Anwendung ausfuehren ----
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
COPY --from=build /workspace/target/*.jar app.jar
USER spring

EXPOSE 8080
HEALTHCHECK --interval=10s --timeout=3s --start-period=30s --retries=3 \
    CMD wget -q -O /dev/null http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
