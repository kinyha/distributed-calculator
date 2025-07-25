FROM openjdk:17-jdk-slim

WORKDIR /app

COPY gradlew .
COPY gradle/ gradle/
RUN chmod +x ./gradlew
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Download dependencies
RUN ./gradlew build --no-daemon || return 0

COPY src src/

RUN ./gradlew build --no-daemon

EXPOSE 8081

CMD ["java", "-jar", "build/libs/addition-service-1.0.0.jar"]