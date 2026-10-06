FROM eclipse-temurin:21-jre
WORKDIR /app
RUN apt-get update \
	&& apt-get install -y --no-install-recommends curl \
	&& rm -rf /var/lib/apt/lists/*
COPY target/order-service-1.0.0.jar /app/order-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/order-service.jar"]
