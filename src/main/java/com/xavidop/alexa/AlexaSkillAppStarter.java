package com.xavidop.alexa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class AlexaSkillAppStarter extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AlexaSkillAppStarter.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(AlexaSkillAppStarter.class, args);
	}
}
