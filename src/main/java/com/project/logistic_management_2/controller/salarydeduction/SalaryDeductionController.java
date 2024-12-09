package com.project.logistic_management_2.controller.salarydeduction;

import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.entity.SalaryDeduction;
import com.project.logistic_management_2.service.salarydeduction.SalaryDeductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/salary-deduction")
public class SalaryDeductionController {

    @Autowired
    private SalaryDeductionService salaryDeductionService;

    @GetMapping
    public ResponseEntity<BaseResponse<SalaryDeduction>> getSalaryDeductionByUserIdAndPeriod(@RequestParam String UserId, @RequestParam String period){
        return ResponseEntity.ok(BaseResponse.ok(salaryDeductionService.getSalaryDeductionByUserIdAndPeriod(UserId, period)));
    }

    @GetMapping("/test-create-salary-deduction")
    public ResponseEntity<BaseResponse<Boolean>> testCreateSalary(){
        salaryDeductionService.createSalaryDeductionForAllUsers();
        return ResponseEntity.ok(BaseResponse.ok(true));
    }
}
