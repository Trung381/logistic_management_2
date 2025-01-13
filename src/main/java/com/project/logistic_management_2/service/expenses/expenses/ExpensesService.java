package com.project.logistic_management_2.service.expenses.expenses;

import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.dto.expenses.ExpensesIncurredDTO;
import com.project.logistic_management_2.dto.expenses.ExpensesReportDTO;
import com.project.logistic_management_2.entity.Expenses;
import org.springframework.web.multipart.MultipartFile;

import java.rmi.ServerException;
import java.sql.Timestamp;
import java.util.List;

public interface ExpensesService {
    List<ExpensesDTO> getAll(Integer page, String expensesConfigId, String truckLicense, String fromDateStr, String toDateStr);
    ExpensesDTO getByID(String id);
    ExpensesDTO create(ExpensesDTO dto) throws ServerException;
    ExpensesDTO update(String id, ExpensesDTO dto);
    long deleteByID(String id) throws ServerException;
    long approveByID(String id) throws ServerException;
    List<ExpensesIncurredDTO> report(String driverId, String period);
    List<ExpensesReportDTO> reportForAll(String period);
    List<Expenses> importExpensesData(MultipartFile importFile);
    ExportExcelResponse exportExpenses(String expensesConfigId, String truckLicense, String fromDate, String toDate) throws Exception;
    ExportExcelResponse exportReportExpenses(String driverId, String period) throws Exception;
}
