package com.project.logistic_management_2.service.report;

import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.dto.report.ReportDetailSalaryDTO;
import com.project.logistic_management_2.dto.report.SummarySalaryDTO;

import java.util.List;

public interface ReportService {
    ReportDetailSalaryDTO getReport(String userId, String period);
    List<SummarySalaryDTO> getSummarySalaryReport(String period);
    ExportExcelResponse exportReport(String userId, String period) throws Exception;
    ExportExcelResponse exportSummarySalaryReport(String period) throws Exception;
}
