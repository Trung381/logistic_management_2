package com.project.logistic_management_2.repository.expenses.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.dto.expenses.ExpensesIncurredDTO;
import com.project.logistic_management_2.dto.expenses.ExpensesReportDTO;

import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface ExpensesRepoCustom {
    List<ExpensesDTO> getAll(String expensesConfigId, String truckLicense, Timestamp fromDate, Timestamp toDate);
    List<ExpensesIncurredDTO> getByFilter(String driverId, YearMonth period);
    Optional<ExpensesDTO> getByID(String id);
    long delete(String id);
    long approve(String id);
    List<ExpensesReportDTO> reportForAll(String period);
    long countByID(String id);
    boolean checkApproved(String id);
}
