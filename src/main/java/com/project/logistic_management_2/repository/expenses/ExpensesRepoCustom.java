package com.project.logistic_management_2.repository.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.entity.Expenses;

import java.util.List;
import java.util.Optional;

public interface ExpensesRepoCustom {
    List<ExpensesDTO> getAll();
    Optional<ExpensesDTO> getByID(String id);
    long delete(String id);
    long approve(String id);
}
