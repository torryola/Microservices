@ECHO OFF
mvn clean install -Dmaven.test.skip=true -f ./pom.xml && ^
docker build -t appuser .
