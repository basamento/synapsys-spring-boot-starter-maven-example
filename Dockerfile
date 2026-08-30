# syntax=docker/dockerfile:1

# The starter is resolved from Maven Central, exactly as a user would.
# JDK 21 is pinned deliberately: Lombok's annotation processing is version
# sensitive and breaks on newer JDKs.

FROM eclipse-temurin:21-jdk AS build

# Capped so a build on a small VPS cannot starve the containers around it.
ENV MAVEN_OPTS="-Xmx512m"

WORKDIR /src

COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

COPY src ./src

RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw -B -ntp -DskipTests package \
    && cp target/example-*.jar /app.jar

FROM eclipse-temurin:21-jre

WORKDIR /app

# SerialGC and a small stack keep the footprint down; the demo is not
# throughput sensitive. Override JAVA_OPTS from Compose to tune.
ENV JAVA_OPTS="-XX:MaxRAMPercentage=70 -XX:+UseSerialGC -Xss512k"

COPY --from=build /app.jar /app/app.jar

RUN useradd --system --uid 10001 worker
USER worker

EXPOSE 8081

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
