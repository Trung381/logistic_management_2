package com.project.logistic_management_2.service.report;

import com.project.logistic_management_2.dto.report.ReportDetailSalaryDTO;
import com.project.logistic_management_2.dto.report.SummarySalaryDTO;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.exception.define.InvalidParameterException;
import com.project.logistic_management_2.repository.report.ReportRepo;
import com.project.logistic_management_2.service.BaseService;
import com.project.logistic_management_2.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl extends BaseService implements ReportService {
    private final ReportRepo reportRepo;
    private final PermissionType type = PermissionType.REPORTS;

    @Override
    public ReportDetailSalaryDTO getReport(String userId, String period){
        checkPermission(type, PermissionKey.VIEW);
        if (userId.isEmpty()) {
            throw new InvalidParameterException("UserId không hợp lệ!");
        }
        Date fromDate = Utils.convertToDate(period);
        Date toDate = Utils.convertToDateOfNextMonth(period);
        return reportRepo.getReport(userId, period, fromDate, toDate);
    }

    public List<SummarySalaryDTO> getSummarySalaryReport(String period) {
        checkPermission(type, PermissionKey.VIEW);
        Date fromDate = Utils.convertToDate(period);
        Date toDate = Utils.convertToDateOfNextMonth(period);
        return reportRepo.getSummarySalary(period, fromDate, toDate);
    }
}
