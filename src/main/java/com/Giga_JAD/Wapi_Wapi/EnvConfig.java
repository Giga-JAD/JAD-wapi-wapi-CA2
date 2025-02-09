package com.Giga_JAD.Wapi_Wapi;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {

	private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing() // Ignore missing .env
			.load();

	@Bean
	public Dotenv dotenv() {
		return getDotenv();
	}

	public static String get(String key) {
		return getDotenv().get(key);
	}

	public static Dotenv getDotenv() {
		return dotenv;
	}
}
