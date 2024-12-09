package com.project.logistic_management_2.service.salarydeduction;

import com.project.logistic_management_2.entity.SalaryDeduction;
import org.springframework.stereotype.Service;

@Service
public interface SalaryDeductionService {
    SalaryDeduction getSalaryDeductionByUserIdAndPeriod(String userId, String period);
    void createSalaryDeductionForAllUsers();
}
