package com.project.logistic_management_2.mapper.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.entity.Expenses;
import com.project.logistic_management_2.enums.IDKey;
import com.project.logistic_management_2.enums.expenses.ExpensesStatus;
import com.project.logistic_management_2.exception.define.InvalidFieldException;
import com.project.logistic_management_2.exception.define.NotModifiedException;
import com.project.logistic_management_2.utils.Utils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExpensesMapper {
    public Expenses toExpenses(ExpensesDTO dto) {
        if (dto == null) return null;
        return createExpenses(dto);
    }

    public List<Expenses> toExpensesList(List<ExpensesDTO> dtos) {
        if(dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }
        return dtos.stream().map(this::createExpenses).collect(Collectors.toList());
    }

    private Expenses createExpenses(ExpensesDTO dto) {
        return Expenses.builder()
                .id(Utils.genID(IDKey.EXPENSES))
                .scheduleId(dto.getScheduleId())
                .expensesConfigId(dto.getExpensesConfigId())
                .amount(dto.getAmount())
                .note(dto.getNote())
                .status(ExpensesStatus.PENDING.getValue())
                .deleted(false)
                .createdAt(dto.getCreatedAt() == null ? new Date() : dto.getCreatedAt())
                .updatedAt(new Date())
                .build();
    }

    public void updateExpenses(Expenses expenses, ExpensesDTO dto) {
        if (dto == null) return;
        boolean isUpdated = false, isValidField = false;

        if (dto.getScheduleId() != null) {
            if (!expenses.getScheduleId().equals(dto.getScheduleId())) {
                expenses.setScheduleId(dto.getScheduleId());
                isUpdated = true;
            }
            isValidField = true;
        }
        if (dto.getExpensesConfigId() != null) {
            if (!expenses.getExpensesConfigId().equals(dto.getExpensesConfigId())) {
                expenses.setExpensesConfigId(dto.getExpensesConfigId());
                isUpdated = true;
            }
            isValidField = true;
        }
        if (dto.getAmount() != null) {
            if (!expenses.getAmount().equals(dto.getAmount())) {
                expenses.setAmount(dto.getAmount());
                isUpdated = true;
            }
            isValidField = true;
        }
        if (dto.getNote() != null) {
            if (!expenses.getNote().equals(dto.getNote())) {
                expenses.setNote(dto.getNote());
                isUpdated = true;
            }
            isValidField = true;
        }

        if (isUpdated) {
            expenses.setUpdatedAt(new Date());
        } else if (isValidField) {
            throw new NotModifiedException("Không có sự thay đổi nào của chi phí!");
        } else {
            throw new InvalidFieldException("Trường cần cập nhật không tồn tại trong chi phí!");
        }
    }
}
