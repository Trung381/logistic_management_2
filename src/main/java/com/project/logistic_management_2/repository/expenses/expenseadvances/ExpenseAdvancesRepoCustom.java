package com.project.logistic_management_2.repository.expenses.expenseadvances;

import com.project.logistic_management_2.dto.expenses.ExpenseAdvancesDTO;

import java.util.List;
import java.util.Optional;

public interface ExpenseAdvancesRepoCustom {
    List<ExpenseAdvancesDTO> getAll();
    Optional<ExpenseAdvancesDTO> getByDriverId(String id);
    long deleted(Integer id);
    Optional<ExpenseAdvancesDTO> getExpenseAdvanceById(Integer id);
}
