package com.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	// 리뷰용
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///C:/ThisIsJava/SpringBootWorkspace/Book/uploads/");
        
        // 공지사항용
        registry.addResourceHandler("/uploads2/**")
        .addResourceLocations("file:///C:/ThisIsJava/SpringBootWorkspace/Book/uploads2/");
    }
}
