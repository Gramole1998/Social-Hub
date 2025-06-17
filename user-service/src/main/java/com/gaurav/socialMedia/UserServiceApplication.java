package com.gaurav.socialMedia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@EnableCaching
@SpringBootApplication
@OpenAPIDefinition(
	    info = @Info(
	        title = "Social-Hub User Service API",
	        version = "1.0.0",
	        description = "Microservice for user management in Social-Hub platform"
	    )
	)
public class UserServiceApplication {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(UserServiceApplication.class, args); 

	}

}
