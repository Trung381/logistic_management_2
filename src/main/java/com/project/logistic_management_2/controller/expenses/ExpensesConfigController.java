package com.project.logistic_management_2.controller.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesConfigDTO;
import com.project.logistic_management_2.dto.response.BaseResponse;
import com.project.logistic_management_2.service.expenses.ExpensesConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpensesConfigController {
    private final ExpensesConfigService expensesConfigService;

    @GetMapping("/configs")
    public ResponseEntity<Object> getExpensesConfigs() {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesConfigService.getAll())
        );
    }

    @GetMapping("/configs/{id}")
    public ResponseEntity<Object> getExpensesConfigByID(@PathVariable String id) {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesConfigService.getByID(id))
        );
    }

    @PostMapping("/configs/create")
    public ResponseEntity<Object> createExpensesConfig(@Valid @RequestBody ExpensesConfigDTO dto) {
        return new ResponseEntity<>(
                BaseResponse.ok(expensesConfigService.create(dto)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/configs/update/{id}")
    public ResponseEntity<Object> updateExpensesConfig(@PathVariable String id, @Valid @RequestBody ExpensesConfigDTO dto) {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesConfigService.update(id, dto))
        );
    }

    @GetMapping("/configs/delete/{id}")
    public ResponseEntity<Object> deleteExpensesConfigByID(@PathVariable String id) {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesConfigService.deleteByID(id))
        );
    }
}
