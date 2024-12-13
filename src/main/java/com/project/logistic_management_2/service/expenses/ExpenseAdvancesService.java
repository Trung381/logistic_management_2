package com.project.logistic_management_2.service.expenses;

import com.project.logistic_management_2.dto.expenses.ExpenseAdvancesDTO;

import java.util.List;

public interface ExpenseAdvancesService {
    List<ExpenseAdvancesDTO> getAll();
    ExpenseAdvancesDTO getByDriverId(String id);
    long delete(Integer id);
    ExpenseAdvancesDTO update(Integer id, ExpenseAdvancesDTO dto);
    ExpenseAdvancesDTO create(ExpenseAdvancesDTO dto);
}
