package com.project.logistic_management_2.mapper.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesConfigDTO;
import com.project.logistic_management_2.entity.ExpensesConfig;
import com.project.logistic_management_2.enums.IDKey;
import com.project.logistic_management_2.utils.Utils;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ExpensesConfigMapper {
    public ExpensesConfig toExpensesConfig(ExpensesConfigDTO dto) {
        if (dto == null) return null;
        return ExpensesConfig.builder()
                .id(Utils.genID(IDKey.EXPENSES_CONFIG))
                .type(dto.getType())
                .note(dto.getNote())
                .deleted(false)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
    }

    public void updateExpensesConfig(ExpensesConfig config, ExpensesConfigDTO dto) {
        if (dto == null) return;
        config.setType(dto.getType());
        config.setNote(dto.getNote());
        config.setUpdatedAt(new Date());
    }

    public ExpensesConfigDTO toExpensesConfigDTO(ExpensesConfigDTO dto, ExpensesConfig config) {
        if (config == null) return null;
        if (dto == null) {
            return ExpensesConfigDTO.builder()
                    .id(config.getId())
                    .type(config.getType())
                    .note(config.getNote())
                    .createdAt(config.getCreatedAt())
                    .updatedAt(config.getUpdatedAt())
                    .build();
        }
        dto.setId(config.getId());
        dto.setCreatedAt(config.getCreatedAt());
        dto.setUpdatedAt(config.getUpdatedAt());
        return dto;
    }
}
