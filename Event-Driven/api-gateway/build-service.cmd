@ECHO OFF

set BASEDIR=.././

set SERVICE-DISCOVERY=%BASEDIR%\service-discovery
set API-GATEWAY=%BASEDIR%\api-gateway

set POM=pom.xml
set TARGET=target

ECHO Building Dependent Services and jar...

mvn clean install -Dmaven.test.skip=true -f %API-GATEWAY%\%POM% && ^

echo Starting Services and Gateway... && ^
start "Api-Gateway" java -jar %API-GATEWAY%\%TARGET%\api-gateway.jar