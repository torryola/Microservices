cls && mvn clean install -Dmaven.test.skip=true -f ./pom.xml && ^
mvn test && ^
docker build -t commentservice . && ^
docker tag commentservice torrydocker/commentservice:rmq-1.0.0 && ^
docker push torrydocker/commentservice:rmq-1.0.0