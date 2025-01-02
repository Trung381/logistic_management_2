package com.project.logistic_management_2.dto.report;

import com.project.logistic_management_2.annotations.ExportColumn;
import com.project.logistic_management_2.dto.salary.SalaryDTO;
import com.project.logistic_management_2.dto.salarydeduction.SalaryDeductionDTO;
import com.project.logistic_management_2.dto.salaryreceived.SalaryReceivedDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleSalaryDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReportDetailSalaryDTO {
    private SalaryDTO salary;
    private List<ScheduleSalaryDTO> schedules;
}
