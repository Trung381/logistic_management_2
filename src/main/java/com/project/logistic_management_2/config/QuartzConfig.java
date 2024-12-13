package com.project.logistic_management_2.config;

import com.project.logistic_management_2.job.SalaryJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    // JobDetail cho SalaryReceivedJob
    @Bean
    public JobDetail salaryJobDetail() {
        return JobBuilder.newJob(SalaryJob.class)
                .withIdentity("salaryJob")
                .storeDurably()
                .build();
    }

    // Trigger cho SalaryJob: Chạy vào ngày 1 hàng tháng lúc 00:00
    @Bean
    public Trigger salaryReceivedJobTrigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                .cronSchedule("0 0 0 1 * ?"); // 0h00 ngày 1 hàng tháng

        return TriggerBuilder.newTrigger()
                .forJob(salaryJobDetail())
                .withIdentity("salaryTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }

}
