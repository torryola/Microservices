mvn clean install -Dmaven.test.skip=false -f ./pom.xml && ^
docker build -t productservice . && ^
docker tag productservice:latest torrydocker/productservice && ^
docker push torrydocker/productservice:latest