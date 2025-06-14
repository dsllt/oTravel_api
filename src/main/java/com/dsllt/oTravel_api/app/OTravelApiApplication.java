package com.dsllt.oTravel_api.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@ComponentScan(basePackages = "com.dsllt.oTravel_api")
@EnableJpaRepositories(basePackages = "com.dsllt.oTravel_api.infra")
@EntityScan(basePackages = "com.dsllt.oTravel_api")
public class OTravelApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OTravelApiApplication.class, args);
	}

}
