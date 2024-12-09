package com.project.logistic_management_2.service.salaryreceived;

import com.project.logistic_management_2.entity.SalaryDeduction;
import com.project.logistic_management_2.entity.SalaryReceived;
import org.springframework.stereotype.Service;

@Service
public interface SalaryReceivedService {
    SalaryReceived getSalaryReceivedByUserIdAndPeriod(String userId, String period);
//    SalaryReceived getSalaryReceivedForPreviousMonth(String userId);
    void createSalaryReceivedForAllUsers();
}
