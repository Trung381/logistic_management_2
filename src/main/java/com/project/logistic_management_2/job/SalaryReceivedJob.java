package com.project.logistic_management_2.job;

import com.project.logistic_management_2.service.salaryreceived.SalaryReceivedService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.quartz.Job;

@Component
public class SalaryReceivedJob implements Job {

    @Autowired
    private SalaryReceivedService salaryReceivedService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            salaryReceivedService.createSalaryReceivedForAllUsers();
            System.out.println("SalaryReceivedJob executed successfully.");
        } catch (Exception e) {
            throw new JobExecutionException("Error executing SalaryReceivedJob", e);
        }
    }
}
