ARG JAVA_VERSION=11
ARG APP_NAME=${project.build.finalName}

FROM openjdk:${JAVA_VERSION}

#Copy jar to app dir
COPY ./target/*.jar /app/${APP_NAME}.jar

# Set work dir
WORKDIR /app

# Download remote file
RUN curl -O https://raw.githubusercontent.com/torryola/Microservices/main/External-logback.xml

# Expose Ports
EXPOSE 8481:8481

#ENRTYPOINT
ENTRYPOINT java -jar ${APP_NAME}.jar -Dspring-profiles.active=${PROFILE} --logging.config=External-logback.xml