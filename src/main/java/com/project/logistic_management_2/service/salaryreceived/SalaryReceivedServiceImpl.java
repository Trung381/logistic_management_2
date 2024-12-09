package com.project.logistic_management_2.service.salaryreceived;

import com.project.logistic_management_2.entity.SalaryDeduction;
import com.project.logistic_management_2.entity.SalaryReceived;
import com.project.logistic_management_2.repository.salarydeduction.SalaryDeductionRepo;
import com.project.logistic_management_2.repository.salaryreceived.SalaryReceivedRepo;
import com.project.logistic_management_2.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalaryReceivedServiceImpl extends BaseService implements SalaryReceivedService {

    @Autowired
    private SalaryReceivedRepo salaryReceivedRepo;

    public SalaryReceived getSalaryReceivedByUserIdAndPeriod(String userId, String period){
        return salaryReceivedRepo.getSalaryReceivedByUserIdAndPeriod(userId, period);
    }

//    public SalaryReceived getSalaryReceivedForPreviousMonth(String userId){
//        return salaryReceivedRepo.getSalaryReceivedForPreviousMonth(userId);
//    }

    public void createSalaryReceivedForAllUsers(){
        salaryReceivedRepo.createSalaryReceivedForAllUsers();
    }
}
