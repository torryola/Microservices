#!/usr/bin/env sh
set -ex
# Build app to generate jar and skip test
mvn clean install -Dmaven.test.skip=true -f ./pom.xml
# Run the test
mvn test
# Create docker image
docker build -t appuser .
# Tag
docker tag appuser:latest torrydocker/appuser
# push to dockerhub
docker push torrydocker/appuser:latest
# run image in a container
#docker container run -p 8081:8081 --name=appuser --env PROFILE=dev appuser

