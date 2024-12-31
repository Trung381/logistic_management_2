package com.project.logistic_management_2.dto.expenses;

import com.project.logistic_management_2.annotations.ExportColumn;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpensesIncurredDTO {
//    private String userId;
    private String expensesConfigId;
    @ExportColumn(name = "Loại chi phí")
    private String type; //Loai chi phi
    @ExportColumn(name = "Số tiền")
    private Float amount; //So tien tuong ung
}
