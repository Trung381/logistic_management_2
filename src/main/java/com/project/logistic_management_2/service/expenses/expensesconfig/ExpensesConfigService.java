package com.project.logistic_management_2.service.expenses.expensesconfig;

import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.dto.expenses.ExpensesConfigDTO;
import com.project.logistic_management_2.entity.ExpensesConfig;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExpensesConfigService {
    List<ExpensesConfigDTO> getAll();
    ExpensesConfigDTO getByID(String id);
    ExpensesConfigDTO create(ExpensesConfigDTO dto);
    ExpensesConfigDTO update(String id, ExpensesConfigDTO dto);
    long deleteByID(String id);
    List<ExpensesConfig> importExpensesConfigData(MultipartFile importFile);
    ExportExcelResponse exportExpensesConfig() throws Exception;
}
