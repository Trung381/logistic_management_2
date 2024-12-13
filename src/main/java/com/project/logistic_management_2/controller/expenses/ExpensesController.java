package com.project.logistic_management_2.controller.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.service.expenses.ExpensesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpensesController {
    private final ExpensesService expensesService;

    @GetMapping()
    public ResponseEntity<Object> getExpenses() {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesService.getAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getExpensesByID(@PathVariable String id) {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesService.getByID(id))
        );
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createExpenses(@Valid @RequestBody ExpensesDTO dto) {
        return new ResponseEntity<>(
                BaseResponse.ok(expensesService.create(dto)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Object> updateExpenses(@PathVariable String id, @Valid @RequestBody ExpensesDTO dto) {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesService.update(id, dto))
        );
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Object> deleteExpensesByID(@PathVariable String id) {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesService.deleteByID(id))
        );
    }

    @GetMapping("/approve/{id}")
    public ResponseEntity<Object> approveExpensesByID(@PathVariable String id) {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesService.approveByID(id))
        );
    }

    @GetMapping("/reports")
    public ResponseEntity<Object> exportReport(@RequestParam String driverId, @RequestParam String period) {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesService.report(driverId, period))
        );
    }

    @GetMapping("/reports/all")
    public ResponseEntity<Object> exportReportForAll(@RequestParam String period) {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesService.reportForAll(period))
        );
    }
}
