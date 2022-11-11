ARG JAVA_VERSION=11

FROM openjdk:${JAVA_VERSION}

COPY target/*.jar /usr/app/

WORKDIR /usr/app

# Download remote file
RUN curl -O https://raw.githubusercontent.com/torryola/Microservices/main/External-logback.xml

EXPOSE 8080

ENTRYPOINT java -jar comment-service.jar -Dspring.profiles.active=${PROFILE} --logging.config=External-logback.xml