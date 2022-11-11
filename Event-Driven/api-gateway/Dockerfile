ARG JAVA_VERSION=11
ARG APP_NAME="api-gateway"
FROM openjdk:${JAVA_VERSION}
#Copy the jar to the working dir
COPY ./target/*.jar /app/${APP_NAME}.jar
#Create WorkDir
WORKDIR /app

EXPOSE 9001:9001
# Entrypoint
ENTRYPOINT java -jar ${APP_NAME}.jar -Dspring-profiles.active=${PROFILE:default}