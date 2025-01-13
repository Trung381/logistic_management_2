package com.project.logistic_management_2.service.report;

import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.dto.report.ReportDetailSalaryDTO;
import com.project.logistic_management_2.dto.report.SummarySalaryDTO;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.exception.define.InvalidParameterException;
import com.project.logistic_management_2.exception.define.NotFoundException;
import com.project.logistic_management_2.repository.report.ReportRepo;
import com.project.logistic_management_2.service.BaseService;
import com.project.logistic_management_2.utils.ExcelUtils;
import com.project.logistic_management_2.utils.ExportConfig;
import com.project.logistic_management_2.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl extends BaseService implements ReportService {
    private final ReportRepo reportRepo;
    private final PermissionType type = PermissionType.REPORTS;

    @Override
    public ReportDetailSalaryDTO getReport(String userId, String period) {
        checkPermission(type, PermissionKey.VIEW);
        if (userId.isEmpty()) {
            throw new InvalidParameterException("UserId không hợp lệ!");
        }
        Date[] range = Utils.createDateRange(period);
        return reportRepo.getReport(userId, period, range[0], range[1]);
    }

    public List<SummarySalaryDTO> getSummarySalaryReport(String period) {
        checkPermission(type, PermissionKey.VIEW);
        Date[] range = Utils.createDateRange(period);
        return reportRepo.getSummarySalary(period, range[0], range[1]);
    }

    @Override
    public ExportExcelResponse exportReport(String userId, String period) throws Exception {
        checkPermission(type, PermissionKey.VIEW);
        Date[] range = Utils.createDateRange(period);
        ReportDetailSalaryDTO detailSalaryReport = reportRepo.getReport(userId, period, range[0], range[1]);

        if (detailSalaryReport == null ||
                (detailSalaryReport.getSalary() == null && CollectionUtils.isEmpty(detailSalaryReport.getSchedules()))) {
            throw new Exception("No data available for the specified user and period.");
        }

        String fileName = "DetailSalaryReport_" + userId + "_" + period + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(List.of(detailSalaryReport), fileName, null);
        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }

    @Override
    public ExportExcelResponse exportSummarySalaryReport(String period) throws Exception {
        checkPermission(type, PermissionKey.VIEW);
        Date[] range = Utils.createDateRange(period);
        List<SummarySalaryDTO> summarySalaryReport = reportRepo.getSummarySalary(period, range[0], range[1]);

        if (CollectionUtils.isEmpty(summarySalaryReport)) {
            throw new NotFoundException("No data");
        }
        String fileName = "SummarySalary Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(summarySalaryReport, fileName, ExportConfig.summarySalaryExport);

        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }
}
