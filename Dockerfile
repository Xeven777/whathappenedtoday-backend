FROM eclipse-temurin:21-jdk as build
WORKDIR /workspace/app

# Copy Maven wrapper and POM first for better layer caching
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies first (will be cached if pom.xml doesn't change)
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline

# Then copy source code
COPY src src

# Build the application
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:21-jre
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Environment variables with defaults
ENV MONGODB_URI=mongodb://localhost:27017 \
    MONGODB_DATABASE=whathappenedtoday \
    SPRING_PROFILES_ACTIVE=prod

# Fix the package path in the entrypoint
ENTRYPOINT ["java", "-cp", "app:app/lib/*", \
            "-Dspring.data.mongodb.uri=${MONGODB_URI}", \
            "-Dspring.data.mongodb.database=${MONGODB_DATABASE}", \
            "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", \
            "com.example.whathappenedtoday.whathappenedtoday_backend.WhathappenedtodayBackendApplication"]

HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

EXPOSE 8080
