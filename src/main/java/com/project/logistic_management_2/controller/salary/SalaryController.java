package com.project.logistic_management_2.controller.salary;

import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.dto.salary.SalaryUserDTO;
import com.project.logistic_management_2.dto.salary.UpdateSalaryDTO;
import com.project.logistic_management_2.service.salary.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salary")
@RequiredArgsConstructor
public class SalaryController {
    private final SalaryService salaryService;

    @GetMapping("/get-all-user")
    public ResponseEntity<BaseResponse<List<SalaryUserDTO>>> getAllSalaryWithUserPeriod(@RequestParam String period){
        return ResponseEntity.ok(
                BaseResponse.ok(salaryService.getAllSalaryWithUserPeriod(period))
        );
    }

    @GetMapping("/get-one")
    public ResponseEntity<BaseResponse<SalaryUserDTO>> getSalaryWithUser(@RequestParam Integer salaryId){
        return ResponseEntity.ok(
                BaseResponse.ok(salaryService.getSalaryWithUser(salaryId))
        );
    }

    @PostMapping("/update")
    public ResponseEntity<BaseResponse<SalaryUserDTO>> updateSalary(@RequestParam Integer salaryId, @RequestBody UpdateSalaryDTO updateSalaryDTO){
        return ResponseEntity.ok(
                BaseResponse.ok(salaryService.updateSalary(salaryId, updateSalaryDTO))
        );
    }

    @GetMapping("/create-all-user")
    public ResponseEntity<BaseResponse<String>> createSalaryForAllUsers(){
        salaryService.createSalaryForAllUsers();
        return ResponseEntity.ok(
                BaseResponse.ok("Tạo thành công lương tháng cho tất cả user với giá trị default rồi he")
        );
    }
}
