package net.torrydev.microservices.reviewsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
public class ReviewsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewsServiceApplication.class, args);
	}
}
