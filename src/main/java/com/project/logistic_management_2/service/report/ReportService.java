package com.project.logistic_management_2.service.report;

import com.project.logistic_management_2.dto.report.ReportDetailSalaryDTO;
import com.project.logistic_management_2.dto.user.UserReportDTO;

import java.util.List;

public interface ReportService {
    ReportDetailSalaryDTO getReport(String userId, String period);
    List<UserReportDTO> getSumarySalaryReport(String period);
}
