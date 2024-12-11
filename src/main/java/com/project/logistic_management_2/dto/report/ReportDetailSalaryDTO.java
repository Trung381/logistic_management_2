package com.project.logistic_management_2.dto.report;

import com.project.logistic_management_2.dto.salarydeduction.SalaryDeductionDTO;
import com.project.logistic_management_2.dto.salaryreceived.SalaryReceivedDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleSalaryDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReportDetailSalaryDTO {
    private SalaryDeductionDTO salaryDeduction;
    private SalaryReceivedDTO salaryReceived;
    private List<ScheduleSalaryDTO> schedules;
}
