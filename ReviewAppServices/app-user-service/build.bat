@ECHO OFF
mvn clean install -Dmaven.test.skip=true -f ./pom.xml && ^
mvn test && ^
docker build -t appuser . && ^
docker tag appuser:latest torrydocker/appuser && ^
docker push torrydocker/appuser:latest

