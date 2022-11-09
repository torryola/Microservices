cls && mvn clean install -Dmaven.test.skip=false -f ./pom.xml && ^
docker build -t productservice . && ^
docker tag productservice torrydocker/productservice:rmq-1.0.0 && ^
docker push torrydocker/productservice:rmq-1.0.0