package com.project.logistic_management_2.mapper.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesConfigDTO;
import com.project.logistic_management_2.entity.expenses.ExpensesConfig;
import com.project.logistic_management_2.enums.IDKey;
import com.project.logistic_management_2.utils.Utils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExpensesConfigMapper {
    public ExpensesConfig toExpensesConfig(ExpensesConfigDTO dto) {
        if (dto == null) return null;
        return ExpensesConfig.builder()
                .id(Utils.genID(IDKey.EXPENSES_CONFIG))
                .type(dto.getType())
                .note(dto.getNote())
                .build();
    }

    public List<ExpensesConfig> toExpensesConfigList(List<ExpensesConfigDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }
        return dtos.stream().map(this::toExpensesConfig).collect(Collectors.toList());
    }

    public void updateExpensesConfig(ExpensesConfig config, ExpensesConfigDTO dto) {
        if (dto == null) return;

        if (dto.getType() != null && !dto.getType().equals(config.getType())) {
            config.setType(dto.getType());
        }
        if (dto.getNote() != null && !dto.getNote().equals(config.getNote())) {
            config.setNote(dto.getNote());
        }
    }
}
