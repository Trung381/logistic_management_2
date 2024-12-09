package com.project.logistic_management_2.config;

import com.project.logistic_management_2.job.SalaryReceivedJob;
import com.project.logistic_management_2.job.SalaryDeductionJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    // JobDetail cho SalaryReceivedJob
    @Bean
    public JobDetail salaryReceivedJobDetail() {
        return JobBuilder.newJob(SalaryReceivedJob.class)
                .withIdentity("salaryReceivedJob")
                .storeDurably()
                .build();
    }

    // Trigger cho SalaryReceivedJob: Chạy vào ngày 1 hàng tháng lúc 00:00
    @Bean
    public Trigger salaryReceivedJobTrigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                .cronSchedule("0 0 0 1 * ?"); // 0h00 ngày 1 hàng tháng

        return TriggerBuilder.newTrigger()
                .forJob(salaryReceivedJobDetail())
                .withIdentity("salaryReceivedTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }

    // JobDetail cho SalaryDeductionJob
    @Bean
    public JobDetail salaryDeductionJobDetail() {
        return JobBuilder.newJob(SalaryDeductionJob.class)
                .withIdentity("salaryDeductionJob")
                .storeDurably()
                .build();
    }

    // Trigger cho SalaryDeductionJob: Chạy vào ngày 1 hàng tháng lúc 00:05
    @Bean
    public Trigger salaryDeductionJobTrigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                .cronSchedule("0 5 0 1 * ?"); // 0h05 ngày 1 hàng tháng

        return TriggerBuilder.newTrigger()
                .forJob(salaryDeductionJobDetail())
                .withIdentity("salaryDeductionTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
