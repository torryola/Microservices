mvn clean install -Dmaven.test.skip=false -f ./pom.xml && ^
docker build -t api-gateway . && ^
docker tag torrydocker/api-gateway:latest torrydocker/api-gateway && ^
docker push torrydocker/api-gateway:latest