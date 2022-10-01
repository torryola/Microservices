#!/usr/bin/env sh
set -ex
# Build app to generate jar and skip test
mvn clean install -Dmaven.test.skip=true -f ./pom.xml
# Run the test
mvn test
# Create docker image
docker build -t reviews-service .
# Tag
docker tag reviews-service:latest torrydocker/reviews-service
# push to dockerhub
docker push torrydocker/reviews-service:latest
# run image in a container
#docker container run -p 8081:8081 --name=reviews-service --env PROFILE=dev appuser

