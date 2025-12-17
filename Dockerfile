FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built JAR from the Maven target directory.
# The name is derived from pom.xml: artifactId = RestaurantQueueSystem, version = 1.0.0
COPY target/RestaurantQueueSystem-1.0.0.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
