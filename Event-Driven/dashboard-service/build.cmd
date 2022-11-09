@ECHO OFF

cls && mvn clean install -Dmaven.skip.test=true && ^
docker build -t dashboard . && ^
docker tag dashboard torrydocker/dashboard:rmq-1.0.0 && ^
docker push torrydocker/dashboard:rmq-1.0.0