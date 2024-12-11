package com.project.logistic_management_2.mapper.user;

import com.project.logistic_management_2.dto.user.UserReportDTO;

public class UserReportMapper {
    public UserReportDTO toUserReportDTO(String userId,
                                         String name,
                                         Float sumTotalSchedules,
                                         Float sumSalaryDeduction,
                                         Float sumSalaryReceived,
                                         Float netSalary) {
        UserReportDTO dto = new UserReportDTO();
        dto.setUserId(userId);
        dto.setName(name);
        dto.setSumTotalSchedules(sumTotalSchedules);
        dto.setSumSalaryDeduction(sumSalaryDeduction);
        dto.setSumSalaryReceived(sumSalaryReceived);
        dto.setNetSalary(netSalary);
        return dto;
    }
}
