package com.project.logistic_management_2.repository.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.dto.expenses.ExpensesReportDTO;
import com.project.logistic_management_2.entity.Expenses;

import java.sql.Date;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface ExpensesRepoCustom {
    List<ExpensesDTO> getAll(String driverId, YearMonth period);
    Optional<ExpensesDTO> getByID(String id);
    long delete(String id);
    long approve(String id);
    List<ExpensesReportDTO> reportForAll(String period);
}
