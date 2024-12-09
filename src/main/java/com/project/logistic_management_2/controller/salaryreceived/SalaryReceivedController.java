package com.project.logistic_management_2.controller.salaryreceived;

import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.entity.SalaryDeduction;
import com.project.logistic_management_2.entity.SalaryReceived;
import com.project.logistic_management_2.service.salarydeduction.SalaryDeductionService;
import com.project.logistic_management_2.service.salaryreceived.SalaryReceivedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/salary-received")
public class SalaryReceivedController {

    @Autowired
    private SalaryReceivedService salaryReceivedService;

    @GetMapping("/user-this-month")
    public ResponseEntity<BaseResponse<SalaryReceived>> getSalaryReceivedByUserIdAndPeriod(@RequestParam String UserId, @RequestParam String period){
        return ResponseEntity.ok(BaseResponse.ok(salaryReceivedService.getSalaryReceivedByUserIdAndPeriod(UserId, period)));
    }

//    @GetMapping("/user-previous-month")
//    public ResponseEntity<BaseResponse<SalaryReceived>> getSalaryReceivedForPreviousMonth(@RequestParam String UserId){
//        return ResponseEntity.ok(BaseResponse.ok(salaryReceivedService.getSalaryReceivedForPreviousMonth(UserId)));
//    }

    @GetMapping("/test-create-salary-received")
    public ResponseEntity<BaseResponse<Boolean>> testCreateSalary(){
        salaryReceivedService.createSalaryReceivedForAllUsers();
        return ResponseEntity.ok(BaseResponse.ok(true));
    }
}
