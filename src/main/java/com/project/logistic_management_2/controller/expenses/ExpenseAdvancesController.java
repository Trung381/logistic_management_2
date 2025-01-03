package com.project.logistic_management_2.controller.expenses;

import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.dto.expenses.ExpenseAdvancesDTO;
import com.project.logistic_management_2.service.expenses.expenseadvances.ExpenseAdvancesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expenses/advances")
@RequiredArgsConstructor
public class ExpenseAdvancesController {
    private final ExpenseAdvancesService expenseAdvancesService;

    @GetMapping()
    public ResponseEntity<Object> getExpenseAdvances() {
        return ResponseEntity.ok(
                BaseResponse.ok(expenseAdvancesService.getAll())
        );
    }

    @GetMapping("/{driverId}")
    public ResponseEntity<Object> getExpenseAdvancesByID(@PathVariable String driverId) {
        return ResponseEntity.ok(
                BaseResponse.ok(expenseAdvancesService.getByDriverId(driverId))
        );
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createExpenseAdvances(@Valid @RequestBody ExpenseAdvancesDTO dto) {
        return new ResponseEntity<>(
                BaseResponse.ok(expenseAdvancesService.create(dto)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Object> updateExpenseAdvances(@PathVariable Integer id, @Valid @RequestBody ExpenseAdvancesDTO dto) {
        return ResponseEntity.ok(
                BaseResponse.ok(expenseAdvancesService.update(id, dto))
        );
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Object> deleteExpenseAdvancesByID(@PathVariable Integer id) {
        return ResponseEntity.ok(
                BaseResponse.ok(expenseAdvancesService.delete(id))
        );
    }
}
