package com.project.logistic_management_2.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SummarySalaryDTO {
    private String userId;
    private String name;
    private Float sumTotalSchedules;
    private Float sumSalaryDeduction;
    private Float sumSalaryReceived;
    private Float netSalary;
}
