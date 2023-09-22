package com.martinPluhar.Bankapplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Spring-boot Bank project",
				description = "Project for job apply in KB bank CZ",
				version = "v1.0.0",
				contact = @Contact(
						name = "Martin Pluhař",
						email = "pluhar.martin@outlook.com",
						url =  "https://github.com/ArmadOon/demo-bank-app"
				),
				license = @License(
						name = "Martin Pluhař",
						url =  "https://github.com/ArmadOon/demo-bank-app"
				)


		),
		externalDocs = @ExternalDocumentation(
				description = "Bank application for apply job to KB bank",
				url =  "https://github.com/ArmadOon/demo-bank-app"
		)
)
public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

}
