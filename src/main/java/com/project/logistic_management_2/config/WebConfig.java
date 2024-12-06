package com.project.logistic_management_2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // URL bắt đầu bằng /uploads/ sẽ trỏ đến thư mục upload trên hệ thống file
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/Users/trung/IdeaProjects/logistic_management_2/src/main/resources/uploads/");
    }
}
