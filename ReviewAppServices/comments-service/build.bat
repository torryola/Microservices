cls && mvn clean install -Dmaven.test.skip=true -f ./pom.xml && ^
mvn test && ^
docker build -t commentservice . && ^
docker tag commentservice:latest torrydocker/commentservice && ^
docker push torrydocker/commentservice:latest