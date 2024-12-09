package com.project.logistic_management_2.repository.salarydeduction;

import com.project.logistic_management_2.entity.SalaryDeduction;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryDeductionRepoCustom {
    SalaryDeduction getSalaryDeductionByUserIdAndPeriod(String userId, String period);
    void createSalaryDeductionForAllUsers();
}
