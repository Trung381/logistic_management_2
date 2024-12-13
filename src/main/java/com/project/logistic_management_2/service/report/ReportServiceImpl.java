package com.project.logistic_management_2.service.report;

import com.project.logistic_management_2.dto.report.ReportDetailSalaryDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleSalaryDTO;
import com.project.logistic_management_2.dto.report.SummarySalaryDTO;
import com.project.logistic_management_2.entity.*;
import com.project.logistic_management_2.enums.PermissionKey;
import com.project.logistic_management_2.enums.PermissionType;
import com.project.logistic_management_2.mapper.user.UserReportMapper;
import com.project.logistic_management_2.repository.report.ReportRepo;
import com.project.logistic_management_2.service.BaseService;
import com.project.logistic_management_2.service.schedule.ScheduleService;
import com.project.logistic_management_2.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public ReportDetailSalaryDTO getReport(String userId, String period){
        checkPermission(type, PermissionKey.VIEW);
        validateUserIdAndPeriod(userId,period);
        return reportRepo.getReport(userId, period);
    }

    public List<SummarySalaryDTO> getSummarySalaryReport(String period) {
        checkPermission(type, PermissionKey.VIEW);
        validatePeriod(period);
        return reportRepo.getSummarySalary(period);
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
