package com.project.logistic_management_2.dto.expenses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpensesIncurredDTO {
//    private String userId;
    private String expensesConfigId;
    private String type; //Loai chi phi
    private Float amount; //So tien tuong ung
}
