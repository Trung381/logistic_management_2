package com.project.logistic_management_2.repository.expenses.expensesconfig;

import com.project.logistic_management_2.dto.expenses.ExpensesConfigDTO;

import java.util.List;
import java.util.Optional;

public interface ExpensesConfigRepoCustom {
    List<ExpensesConfigDTO> getAll();
    Optional<ExpensesConfigDTO> getByID(String id);
    long delete(String id);
}
