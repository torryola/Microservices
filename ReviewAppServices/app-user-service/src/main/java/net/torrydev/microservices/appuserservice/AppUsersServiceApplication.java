package net.torrydev.microservices.appuserservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableEurekaClient
@Slf4j
public class AppUsersServiceApplication {
	@Autowired
	static Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(AppUsersServiceApplication.class, args);

		System.out.println("Logback Config Dir ===>> "+environment.getProperty("logging.config"));
		System.out.println("Logback Config Dir ===>> "+environment.getProperty("LOG_BACK_CONFIG_DIR"));
	}
}
