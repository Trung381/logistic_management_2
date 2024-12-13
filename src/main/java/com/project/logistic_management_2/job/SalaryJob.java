package com.project.logistic_management_2.job;


import com.project.logistic_management_2.service.salary.SalaryService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SalaryJob implements Job {
    @Autowired
    private SalaryService salaryService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            salaryService.createSalaryForAllUsers();
            System.out.println("SalaryDeductionJob executed successfully.");
        } catch (Exception e) {
            throw new JobExecutionException("Error executing SalaryDeductionJob", e);
        }
    }
}
