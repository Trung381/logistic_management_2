package com.project.logistic_management_2.service.expenses.expenseadvances;

import com.project.logistic_management_2.dto.expenses.ExpenseAdvancesDTO;

import java.util.List;

public interface ExpenseAdvancesService {
    List<ExpenseAdvancesDTO> getAll(int page);
    ExpenseAdvancesDTO getByDriverId(String id);
    long delete(Integer id);
    ExpenseAdvancesDTO update(Integer id, ExpenseAdvancesDTO dto);
    ExpenseAdvancesDTO create(ExpenseAdvancesDTO dto);
}
