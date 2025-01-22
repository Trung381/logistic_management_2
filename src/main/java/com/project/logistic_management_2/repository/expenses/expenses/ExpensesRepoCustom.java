package com.project.logistic_management_2.repository.expenses.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.dto.expenses.ExpensesIncurredDTO;
import com.project.logistic_management_2.dto.expenses.ExpensesReportDTO;
import com.project.logistic_management_2.enums.expenses.ExpensesStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ExpensesRepoCustom {
    List<ExpensesDTO> getAll(int page, String expensesConfigId, String truckLicense, Date fromDate, Date toDate);
    List<ExpensesDTO> getAll(String expensesConfigId, String truckLicense, Date fromDate, Date toDate);
    List<ExpensesIncurredDTO> getExpenseIncurredByDriverID(String driverId, Date fromDate, Date toDate);
    Optional<ExpensesDTO> getByID(String id);
    long delete(String id);
    long approve(String id);
    List<ExpensesReportDTO> reportAll(String period);
    long countByID(String id);
    ExpensesStatus getStatusByID(String id);
}
