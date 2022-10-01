#!/usr/bin/env sh
set -ex
# Build app to generate jar and skip test
mvn clean install -Dmaven.test.skip=true -f ./pom.xml
# Run the test
mvn test
# Create docker image
docker build -t ratingservice .
# Tag
docker tag ratingservice:latest torrydocker/ratingservice
# push to dockerhub
docker push torrydocker/ratingservice:latest
# run image in a container
#docker container run -p 8081:8081 --name=ratingservice --env PROFILE=dev appuser

