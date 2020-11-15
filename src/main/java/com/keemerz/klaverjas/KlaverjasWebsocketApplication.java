package com.keemerz.klaverjas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class KlaverjasWebsocketApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(KlaverjasWebsocketApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(KlaverjasWebsocketApplication.class, args);
	}
}
