package com.project.logistic_management_2.service.expenses.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.dto.expenses.ExpensesIncurredDTO;
import com.project.logistic_management_2.dto.expenses.ExpensesReportDTO;
import com.project.logistic_management_2.entity.Expenses;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

public interface ExpensesService {
    List<ExpensesDTO> getAll(int page, String expensesConfigId, String truckLicense, Timestamp fromDate, Timestamp toDate);
    List<ExpensesDTO> getAll(String expensesConfigId, String truckLicense, Timestamp fromDate, Timestamp toDate);
    ExpensesDTO getByID(String id);
    ExpensesDTO create(ExpensesDTO dto);
    ExpensesDTO update(String id, ExpensesDTO dto);
    long deleteByID(String id);
    long approveByID(String id);
    List<ExpensesIncurredDTO> report(String driverId, String period);
    List<ExpensesReportDTO> reportForAll(String period);
    List<Expenses> importExpensesData(MultipartFile importFile);
}
