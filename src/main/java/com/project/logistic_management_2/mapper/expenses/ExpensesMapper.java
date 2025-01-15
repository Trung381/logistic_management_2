package com.project.logistic_management_2.mapper.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.entity.expenses.Expenses;
import com.project.logistic_management_2.enums.IDKey;
import com.project.logistic_management_2.enums.expenses.ExpensesStatus;
import com.project.logistic_management_2.utils.Utils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExpensesMapper {
    public Expenses toExpenses(ExpensesDTO dto) {
        if (dto == null) return null;
        return Expenses.builder()
                .id(Utils.genID(IDKey.EXPENSES))
                .scheduleId(dto.getScheduleId())
                .expensesConfigId(dto.getExpensesConfigId())
                .amount(dto.getAmount())
                .note(dto.getNote())
                .status(ExpensesStatus.PENDING.getValue())
                .build();
    }

    public List<Expenses> toExpensesList(List<ExpensesDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }
        return dtos.stream().map(this::toExpenses).collect(Collectors.toList());
    }

    public void updateExpenses(Expenses expenses, ExpensesDTO dto) {
        if (dto == null) return;

        if (dto.getScheduleId() != null && !dto.getScheduleId().equals(expenses.getScheduleId())) {
            expenses.setScheduleId(dto.getScheduleId());
        }
        if (dto.getExpensesConfigId() != null && !dto.getExpensesConfigId().equals(expenses.getExpensesConfigId())) {
            expenses.setExpensesConfigId(dto.getExpensesConfigId());
        }
        if (dto.getAmount() != null && !dto.getAmount().equals(expenses.getAmount())) {
            expenses.setAmount(dto.getAmount());
        }
        if (dto.getNote() != null && !dto.getNote().equals(expenses.getNote())) {
            expenses.setNote(dto.getNote());
        }
    }
}
