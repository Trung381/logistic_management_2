package com.project.logistic_management_2.repository.salaryreceived;

import com.project.logistic_management_2.entity.SalaryDeduction;
import com.project.logistic_management_2.entity.SalaryReceived;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryReceivedRepoCustom {
    SalaryReceived getSalaryReceivedByUserIdAndPeriod(String userId, String period);
//    SalaryReceived getSalaryReceivedForPreviousMonth(String userId);
    void createSalaryReceivedForAllUsers();
}
