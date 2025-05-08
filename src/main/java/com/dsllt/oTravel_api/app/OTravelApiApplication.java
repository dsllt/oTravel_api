package com.dsllt.oTravel_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@ComponentScan(basePackages = "com.dsllt.oTravel_api")
//@EnableJpaRepositories(basePackages = {
//		"com.dsllt.oTravel_api.infra.adapter.favorite.out.persistence.jpa",
//		"com.dsllt.oTravel_api.infra.adapter.user.out.persistence.jpa"
//})
//@EntityScan(basePackages = "com.dsllt.oTravel_api.infra.adapter.user.out.persistence.jpa")
public class OTravelApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OTravelApiApplication.class, args);
	}

}
