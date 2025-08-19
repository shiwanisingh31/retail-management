package com.retail_management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowCredentials(true)
		// Support localhost and 127.0.0.1 during local development
		.allowedOriginPatterns("http://localhost:3000", "http://127.0.0.1:3000")
		.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
		.allowedHeaders("*")
		.exposedHeaders("Set-Cookie")
		.maxAge(3600);
	}
}


