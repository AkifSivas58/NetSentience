# Use Eclipse Temurin (The new standard for OpenJDK)
FROM eclipse-temurin:21-jdk

# Install ping (It's missing in minimal images)
# We use apt-get because this image is Ubuntu/Debian based
RUN apt-get update && apt-get install -y iputils-ping && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the built JAR file
COPY target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]