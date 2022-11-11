@ECHO OFF
cls && mvn clean install -Dmaven.test.skip=false -f ./pom.xml && ^
mvn test && ^
docker build -t appuser . && ^
docker tag appuser torrydocker/appuser:rmq-1.0.0 && ^
docker push torrydocker/appuser:rmq-1.0.0

