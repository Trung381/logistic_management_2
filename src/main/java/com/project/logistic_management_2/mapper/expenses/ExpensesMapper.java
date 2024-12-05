package com.project.logistic_management_2.mapper.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesConfigDTO;
import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.entity.Expenses;
import com.project.logistic_management_2.enums.IDKey;
import com.project.logistic_management_2.utils.Utils;
import org.springframework.stereotype.Component;

import java.util.Date;

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
                .imgPath(dto.getImgPath())
                .status(0)
                .deleted(false)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
    }

    public void updateExpenses(String id, Expenses expenses, ExpensesDTO dto) {
        if (dto == null) return;
        expenses.setId(id);
        expenses.setScheduleId(dto.getScheduleId());
        expenses.setExpensesConfigId(dto.getExpensesConfigId());
        expenses.setAmount(dto.getAmount());
        expenses.setNote(dto.getNote());
        expenses.setImgPath(dto.getImgPath());
        expenses.setUpdatedAt(new Date());
    }
}
