@ECHO OFF

set BASEDIR=.././

set SERVICE-DISCOVERY=%BASEDIR%\service-discovery
set API-GATEWAY=%BASEDIR%\api-gateway
set APP-SERVICE=%BASEDIR%\app-user-service
set PRODUCT-SERVICE=%BASEDIR%\products-service
set COMMENT-SERVICE=%BASEDIR%\comments-service
set RATING-SERVICE=%BASEDIR%\ratings-service

set POM=pom.xml
set TARGET=target

ECHO Building Dependent Services...

mvn clean install -Dmaven.test.skip=true -f %SERVICE-DISCOVERY%\%POM% && ^
mvn clean install -Dmaven.test.skip=true -f %APP-SERVICE%\%POM% && ^
mvn clean install -Dmaven.test.skip=true -f %PRODUCT-SERVICE%\%POM% && ^
mvn clean install -Dmaven.test.skip=true -f %COMMENT-SERVICE%\%POM% && ^
mvn clean install -Dmaven.test.skip=true -f %RATING-SERVICE%\%POM% && ^
mvn clean install -Dmaven.test.skip=true -f %API-GATEWAY%\%POM% && ^

ECHO Starting zipkin service... && ^
docker run -p 9411:9411 openzipkin/zipkin && ^

echo Starting Services and Gateway... && ^
start "Eureka" java -jar %SERVICE-DISCOVERY%\%TARGET%\eureka-server.jar && ^
start "App-Service" java -jar %APP-SERVICE%\%TARGET%\app-user.jar && ^
start "Product-Service" java -jar %PRODUCT-SERVICE%\%TARGET%\product-service.jar && ^
start "Comment-Service" java -jar %COMMENT-SERVICE%\%TARGET%\comment-service.jar && ^
start "Rating-Service" java -jar %RATING-SERVICE%\%TARGET%\rating-service.jar && ^
start "Api-Gateway" java -jar %API-GATEWAY%\%TARGET%\api-gateway.jar