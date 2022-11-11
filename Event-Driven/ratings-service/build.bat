
cls && mvn clean install -Dmaven.test.skip=true -f ./pom.xml && ^
mvn test && ^
docker build -t ratingservice . && ^
docker tag ratingservice torrydocker/ratingservice:rmq-1.0.0 && ^
docker push torrydocker/ratingservice:rmq-1.0.0
