package com.project.logistic_management_2.repository.report;



import com.project.logistic_management_2.dto.report.ReportDetailSalaryDTO;
import com.project.logistic_management_2.entity.Permission;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ReportRepo {
    ReportDetailSalaryDTO getReport(String userId, String period);
}
