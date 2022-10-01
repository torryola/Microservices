
mvn clean install -Dmaven.test.skip=true -f ./pom.xml && ^
mvn test && ^
docker build -t ratingservice . && ^
docker tag ratingservice:latest torrydocker/ratingservice && ^
docker push torrydocker/ratingservice:latest
