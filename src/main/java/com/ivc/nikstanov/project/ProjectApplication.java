package com.ivc.nikstanov.project;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Ivc practice documentation",
				description = "Spring boot documentation for microservices project",
				version = "0.0.1",
				contact = @Contact(
						name = "Nikstanov",
						email = "n.stahnov@gmail.com"
				)
		)
)
public class ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}

}
