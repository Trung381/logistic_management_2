package com.project.logistic_management_2.mapper.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.entity.Expenses;
import com.project.logistic_management_2.enums.IDKey;
import com.project.logistic_management_2.enums.expenses.ExpensesStatus;
import com.project.logistic_management_2.exception.def.InvalidFieldException;
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

        return Expenses.builder()
                .id(Utils.genID(IDKey.EXPENSES))
                .scheduleId(dto.getScheduleId())
                .expensesConfigId(dto.getExpensesConfigId())
                .amount(dto.getAmount())
                .note(dto.getNote())
                .imgPath(dto.getImgPath())
                .status(ExpensesStatus.WAITING_FOR_APPROVAL.getValue())
                .deleted(false)
                .createdAt(dto.getCreatedAt() == null ? new Date() : dto.getCreatedAt())
                .updatedAt(new Date())
                .build();
    }

    public List<Expenses> toExpensesList(List<ExpensesDTO> dtos) {
        if(dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }

        return dtos.stream().map(dto ->
                Expenses.builder()
                        .id(Utils.genID(IDKey.EXPENSES))
                        .scheduleId(dto.getScheduleId())
                        .expensesConfigId(dto.getExpensesConfigId())
                        .amount(dto.getAmount())
                        .note(dto.getNote())
                        .imgPath(dto.getImgPath())
                        .status(ExpensesStatus.WAITING_FOR_APPROVAL.getValue())
                        .deleted(false)
                        .createdAt(dto.getCreatedAt() == null ? new Date() : dto.getCreatedAt())
                        .updatedAt(new Date())
                        .build()
        ).collect(Collectors.toList());
    }

    /**
     * Update the fields that need to be updated of the expenses
     *
     * @param expenses: object to be updated
     * @param dto: object contain the values to be updated
     */
    public void updateExpenses(Expenses expenses, ExpensesDTO dto) {
        if (dto == null) return;
        boolean isUpdated = false;

        if (dto.getScheduleId() != null) {
            expenses.setScheduleId(dto.getScheduleId());
            isUpdated = true;
        }
        if (dto.getExpensesConfigId() != null) {
            expenses.setExpensesConfigId(dto.getExpensesConfigId());
            isUpdated = true;
        }
        if (dto.getAmount() != null) {
            expenses.setAmount(dto.getAmount());
            isUpdated = true;
        }
        if (dto.getNote() != null) {
            expenses.setNote(dto.getNote());
            isUpdated = true;
        }
        if (dto.getImgPath() != null) {
            expenses.setImgPath(dto.getImgPath());
            isUpdated = true;
        }

        if (isUpdated) {
            expenses.setUpdatedAt(new Date());
        } else {
            throw new InvalidFieldException("Trường cần cần cập nhật không tồn tại!");
        }
    }
}
