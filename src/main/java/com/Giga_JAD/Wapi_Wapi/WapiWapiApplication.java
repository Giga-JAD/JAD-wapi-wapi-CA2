package com.Giga_JAD.Wapi_Wapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class WapiWapiApplication {

	public static void main(String[] args) {
		Dotenv dotenv = EnvConfig.getDotenv();

		System.setProperty("stripe.secretKey", dotenv.get("stripe_secretKey"));
		System.setProperty("stripe.webhookSecret", dotenv.get("stripe_webhookSecret"));
		System.setProperty("DB_CLASS", dotenv.get("DB_CLASS"));
		System.setProperty("DB_URL", dotenv.get("DB_URL"));
		System.setProperty("DB_USERNAME", dotenv.get("DB_USER"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		System.setProperty("PORT", dotenv.get("PORT"));
		SpringApplication.run(WapiWapiApplication.class, args);
	}

}
