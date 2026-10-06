FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/order-service-1.0.0.jar /app/order-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/order-service.jar"]
