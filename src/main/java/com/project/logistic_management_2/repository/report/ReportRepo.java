package com.project.logistic_management_2.repository.report;



import com.project.logistic_management_2.dto.report.ReportDetailSalaryDTO;
import com.project.logistic_management_2.dto.report.SummarySalaryDTO;
import com.project.logistic_management_2.entity.Permission;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface ReportRepo {
    ReportDetailSalaryDTO getReport(String userId, String period, Date fromDate, Date toDate);
    List<SummarySalaryDTO> getSummarySalary(String period, Date fromDate, Date toDate);
}
