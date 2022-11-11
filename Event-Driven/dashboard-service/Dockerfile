# Build App Image
ARG JAVA_VERSION=11

FROM openjdk:${JAVA_VERSION}

COPY target/dashboard-service.jar /usr/app/
# Copy Log Config - It doesn't work using docker-compose
# This can be externalized i.e. from Repository. curl -O <LINK-TO-THE-FILE>
# Alternatively, it can be pasered as commandline arg when building the image using --build-arg LOG_BACK_CONFIG=<PATH-TO-LOG-BACK-CONFIG-FILE>
#COPY src/main/resources/External-logback.xml /usr/app/

WORKDIR /usr/app
# Download remote file
RUN curl -O https://raw.githubusercontent.com/torryola/Microservices/main/External-logback.xml

EXPOSE 9100

ENTRYPOINT java -jar dashboard-service.jar --spring.profiles.active=${PROFILE} --logging.config=External-logback.xml