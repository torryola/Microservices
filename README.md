# Microservices
The aim of this repo or project is to design a near production grade microservice that can serve as 
template when creating microservices for any project. 
In this project, a simple Review-App-Service will be created. Arguably this can be made monolithic but for demo purposes, it will be a collection of 
components as follows: 
### **Services for**:
- App-User - reviewer 
- Products - product to review
- Comments - ... :point_left: If you like the product comment on it :wink:
- Ratings - ... :point_left: If you like the product comment on it :+1:
- Api-Gateway - entrance to the services :house:
- Service Discovery - just to make our lives a little easier
- Dashboard - for showcasing the msg from RabbitMQ, just for 

### **Tech Stack**:
- Java 11+
- Spring Boot 2.6+
- Spring Cloud
- Mockito, TestEngine and JUnit
- MySql And H2 Databases
- Docker 
- RabbitMQ
- Git
- Maven
- Resilience4j
- Linux
- Distributed Logging (Sleuth/Zipkin)
- Eureka 
