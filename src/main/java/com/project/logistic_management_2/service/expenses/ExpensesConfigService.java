package com.project.logistic_management_2.service.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesConfigDTO;

import java.util.List;

public interface ExpensesConfigService {
    List<ExpensesConfigDTO> getAll();
    ExpensesConfigDTO getByID(String id);
    ExpensesConfigDTO create(ExpensesConfigDTO dto);
    ExpensesConfigDTO update(String id, ExpensesConfigDTO dto);
    long deleteByID(String id);
}
