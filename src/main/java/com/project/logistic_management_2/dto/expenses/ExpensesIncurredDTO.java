package com.project.logistic_management_2.dto.expenses;

import com.project.logistic_management_2.annotations.ExportColumn;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpensesIncurredDTO {
    private String expensesConfigId;
    @ExportColumn(name = "Loại chi phí")
    private String type;
    @ExportColumn(name = "Số tiền")
    private Float amount;
}
