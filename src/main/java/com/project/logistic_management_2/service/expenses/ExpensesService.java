package com.project.logistic_management_2.service.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.dto.expenses.ExpensesReportDTO;
import com.project.logistic_management_2.entity.Expenses;

import java.util.List;

public interface ExpensesService {
    List<ExpensesDTO> getAll();
    ExpensesDTO getByID(String id);
    ExpensesDTO create(ExpensesDTO dto);
    ExpensesDTO update(String id, ExpensesDTO dto);
    long deleteByID(String id);
    long approveByID(String id);
    List<ExpensesDTO> report(String driverId, String period);
    List<ExpensesReportDTO> reportForAll(String period);
}
