package com.project.logistic_management_2.job;

import com.project.logistic_management_2.service.salarydeduction.SalaryDeductionService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.quartz.Job;

@Component
public class SalaryDeductionJob implements Job {

    @Autowired
    private SalaryDeductionService salaryDeductionService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            salaryDeductionService.createSalaryDeductionForAllUsers();
            System.out.println("SalaryDeductionJob executed successfully.");
        } catch (Exception e) {
            throw new JobExecutionException("Error executing SalaryDeductionJob", e);
        }
    }
}
