package com.project.logistic_management_2.repository.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesConfigDTO;
import com.project.logistic_management_2.entity.ExpensesConfig;

import java.util.List;
import java.util.Optional;

public interface ExpensesConfigRepoCustom {
    List<ExpensesConfigDTO> getAll();
    Optional<ExpensesConfig> getByID(String id);
    long delete(String id);
}
