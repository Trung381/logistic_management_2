package com.project.logistic_management_2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LogisticManagement2Application {

    public static void main(String[] args) {
        SpringApplication.run(LogisticManagement2Application.class, args);
    }

}
