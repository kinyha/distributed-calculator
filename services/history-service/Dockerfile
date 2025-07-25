FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy pre-built JAR
COPY build/libs/history-service-1.0.0.jar app.jar

EXPOSE 8085

CMD ["java", "-jar", "app.jar"]