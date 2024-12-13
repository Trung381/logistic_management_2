package com.project.logistic_management_2.config;

import nonapi.io.github.classgraph.json.JSONUtils;
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

        System.err.println(uploadDir);

        String resourceLocation = "file:" + uploadDir;
        if (!uploadDir.endsWith("/")) {
            resourceLocation += "/";
        }

        // URL bắt đầu bằng /uploads/ sẽ trỏ đến thư mục upload trên hệ thống file
        registry.addResourceHandler("/uploads/**")
//                .addResourceLocations("file:/Users/trung/IdeaProjects/logistic_management_2/src/main/resources/uploads/");
                .addResourceLocations(resourceLocation);
    }
}
