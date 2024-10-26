FROM eclipse-temurin:21-jdk
LABEL authors="janalojzgacnik"

ENTRYPOINT ["top", "-b"]

ARG JAR_ACTOR=actor/target/*.jar

# Copy the JAR file from the build context into the Docker image
COPY ${JAR_ACTOR} actor.jar
CMD apt-get update -y

# Set the default command to run the Java application
ENTRYPOINT ["java", "-Xmx4096M", "-Djasypt.encryptor.password=OWh7du07EO", "-jar", "/actor.jar"]