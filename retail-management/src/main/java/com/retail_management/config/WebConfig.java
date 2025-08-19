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
		.allowedOrigins("http://localhost:3000", "http://localhost:8444", "http://localhost:8445", "http://localhost:8446", "http://localhost:8447", "http://localhost:8448")
		.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
	}
}


