package com.project.logistic_management_2.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserReportDTO {
    private String userId;
    private String name;
    private Float sumTotalSchedules;
    private Float sumSalaryDeduction;
    private Float sumSalaryReceived;
    private Float netSalary;
}

