package com.project.logistic_management_2.mapper.user;

import com.project.logistic_management_2.dto.report.SummarySalaryDTO;

public class UserReportMapper {
    public SummarySalaryDTO toUserReportDTO(String userId,
                                            String name,
                                            Float sumTotalSchedules,
                                            Float sumSalaryDeduction,
                                            Float sumSalaryReceived,
                                            Float netSalary) {
        SummarySalaryDTO dto = new SummarySalaryDTO();
        dto.setUserId(userId);
        dto.setName(name);
        dto.setSumTotalSchedules(sumTotalSchedules);
        dto.setSumSalaryDeduction(sumSalaryDeduction);
        dto.setSumSalaryReceived(sumSalaryReceived);
        dto.setNetSalary(netSalary);
        return dto;
    }
}
