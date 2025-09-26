package com.rh.api_rh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class ApiRhApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiRhApplication.class, args);
	}

}
