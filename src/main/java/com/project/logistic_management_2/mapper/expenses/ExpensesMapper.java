package com.project.logistic_management_2.mapper.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.entity.Expenses;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ExpensesMapper {
    Expenses toExpenses(ExpensesDTO dto) {
        if (dto == null) return null;

        return Expenses.builder()
                .scheduleId(dto.getScheduleId())
                .expensesConfigId(dto.getExpensesConfigId())
                .amount(dto.getAmount())
                .note(dto.getNote())
                .status(0)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
    }
}
