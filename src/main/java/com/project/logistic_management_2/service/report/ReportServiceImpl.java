package com.project.logistic_management_2.service.report;

import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.dto.report.ReportDetailSalaryDTO;
import com.project.logistic_management_2.dto.report.SummarySalaryDTO;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.exception.def.NotFoundException;
import com.project.logistic_management_2.repository.report.ReportRepo;
import com.project.logistic_management_2.service.BaseService;
import com.project.logistic_management_2.service.schedule.schedule.ScheduleService;
import com.project.logistic_management_2.service.user.UserService;
import com.project.logistic_management_2.utils.ExcelUtils;
import com.project.logistic_management_2.utils.ExportConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ReportServiceImpl extends BaseService implements ReportService {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReportRepo reportRepo;
    private final PermissionType type = PermissionType.REPORTS;

    @Override
    public ReportDetailSalaryDTO getReport(String userId, String period) {
        checkPermission(type, PermissionKey.VIEW);
        validateUserIdAndPeriod(userId, period);
        return reportRepo.getReport(userId, period);
    }

    public List<SummarySalaryDTO> getSummarySalaryReport(String period) {
        checkPermission(type, PermissionKey.VIEW);
        validatePeriod(period);
        return reportRepo.getSummarySalary(period);
    }

    @Override
    public ExportExcelResponse exportReport(String userId, String period) throws Exception {
        ReportDetailSalaryDTO detailSalaryReport = reportRepo.getReport(userId, period);

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
        List<SummarySalaryDTO> summarySalaryReport = reportRepo.getSummarySalary(period);

        if (CollectionUtils.isEmpty(summarySalaryReport)) {
            throw new NotFoundException("No data");
        }
        String fileName = "SummarySalary Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(summarySalaryReport, fileName, ExportConfig.summarySalaryExport);

        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }

    private void validateUserIdAndPeriod(String userId, String period) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("UserId không hợp lệ!");
        }

        validatePeriod(period);
    }

    private void validatePeriod(String period) {
        String regex = "^(\\d{4}-(0[1-9]|1[0-2]))$";
        if (!Pattern.matches(regex, period)) {
            throw new IllegalArgumentException("Định dạng chu kỳ không hợp lệ! Dạng đúng: yyyy-MM");
        }
    }

    private Float safeValue(Float val) {
        return val == null ? 0.0f : val;
    }
}
