package com.project.logistic_management_2.mapper.expenses;

import com.project.logistic_management_2.dto.expenses.ExpenseAdvancesDTO;
import com.project.logistic_management_2.entity.expenses.ExpenseAdvances;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ExpenseAdvancesMapper {
    public ExpenseAdvances toExpenseAdvances(ExpenseAdvancesDTO dto) {
        if (dto == null) return null;
        return ExpenseAdvances.builder()
                .driverId(dto.getDriverId())
                .period(dto.getPeriod())
                .advance(dto.getAdvance())
                .remainingBalance(0f)
                .note(dto.getNote())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
    }

    public void updateExpenseAdvance(Integer id, ExpenseAdvances expenseAdvances, ExpenseAdvancesDTO dto) {
        if (dto == null || expenseAdvances == null) return;
        expenseAdvances.setId(id);
        expenseAdvances.setDriverId(dto.getDriverId());
        expenseAdvances.setPeriod(dto.getPeriod());
        expenseAdvances.setAdvance(dto.getAdvance());
        expenseAdvances.setRemainingBalance(dto.getRemainingBalance());
        expenseAdvances.setNote(dto.getNote());
        expenseAdvances.setUpdatedAt(new Date());
    }
}
