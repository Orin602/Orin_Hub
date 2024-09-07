package com.demo.config;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	 @Value("${com.demo.upload.path}")
	    private String uploadPath;

	    @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        String uploadDirectory = Paths.get(uploadPath).toUri().toString();
	        if (!uploadDirectory.endsWith("/")) {
	            uploadDirectory += "/";
	        }

	        registry.addResourceHandler("/uploads/**").addResourceLocations("file:uploads/");
	    }
	 
}
