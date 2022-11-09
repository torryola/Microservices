#!/usr/bin/env sh
set -ex
# Build Jar
clear && mvn clean install -Dmaven.skip.test=true
# Build Image
docker build -t dashboard .
# Tag Image before pushing
docker tag dashboard torrydocker/dashboard:rmq-1.0.0
# Push image to remote Docker Repo
docker push torrydocker/dashboard:rmq-1.0.0