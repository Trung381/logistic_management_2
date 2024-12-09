package com.project.logistic_management_2.service.salarydeduction;

import com.project.logistic_management_2.entity.SalaryDeduction;
import com.project.logistic_management_2.repository.salarydeduction.SalaryDeductionRepo;
import com.project.logistic_management_2.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalaryDeductionServiceImpl extends BaseService implements SalaryDeductionService{

    @Autowired
    private SalaryDeductionRepo salaryDeductionRepo;

    public SalaryDeduction getSalaryDeductionByUserIdAndPeriod(String userId, String period){
        return salaryDeductionRepo.getSalaryDeductionByUserIdAndPeriod(userId, period);
    }

    public void createSalaryDeductionForAllUsers(){
        salaryDeductionRepo.createSalaryDeductionForAllUsers();
    }
}
