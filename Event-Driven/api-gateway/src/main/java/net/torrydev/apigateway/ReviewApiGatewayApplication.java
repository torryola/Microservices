package net.torrydev.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ReviewApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewApiGatewayApplication.class, args);
	}

}
