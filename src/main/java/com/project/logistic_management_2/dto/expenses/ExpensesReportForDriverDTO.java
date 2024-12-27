package com.project.logistic_management_2.dto.expenses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ExpensesReportForDriverDTO {
    private String driverId;
    private String driverName;
    private List<ExpensesConfigDTO> lstExpenses;
}

