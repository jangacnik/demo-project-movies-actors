FROM eclipse-temurin:21-jdk
LABEL authors="janalojzgacnik"

ENTRYPOINT ["top", "-b"]

ARG JAR_MOVIE=movie/target/*.jar

# Copy the JAR file from the build context into the Docker image
COPY ${JAR_MOVIE} movie.jar
CMD apt-get update -y

# Set the default command to run the Java application
ENTRYPOINT ["java", "-Xmx4096M", "-Djasypt.encryptor.password=8w6Ib3tWc7", "-jar", "/movie.jar"]