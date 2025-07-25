FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy pre-built JAR
COPY build/libs/api-gateway-1.0.0.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]