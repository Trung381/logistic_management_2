package com.project.logistic_management_2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {


        // URL bắt đầu bằng /uploads/ sẽ trỏ đến thư mục upload trên hệ thống file
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" +uploadDir);
    }
}
