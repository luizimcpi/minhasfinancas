# Start with a base image containing Java runtime
FROM openjdk:17

# Add Maintainer Info
LABEL maintainer="luizhenrique.se@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# The application's jar file
ARG JAR_FILE=jars/minhasfinancas-*.jar

# Add the application's jar to the container
ADD ${JAR_FILE} minhasfinancas.jar

EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=docker","-jar","/minhasfinancas.jar"]