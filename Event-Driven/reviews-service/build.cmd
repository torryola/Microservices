mvn clean install -Dmaven.test.skip=false -f ./pom.xml && ^
docker build -t reviews-service . && ^
docker tag reviews-service:latest torrydocker/review-service && ^
docker push torrydocker/review-service:latest