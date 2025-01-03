package com.project.logistic_management_2.dto.expenses;

import com.project.logistic_management_2.annotations.ExportColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateExpenseDTO {
    private String driverId;
    private String driverName;
    private String expensesConfigId;
    private String expensesConfigType;
    private Float amount;
    private String note;
    private String imgPath;
    private String scheduleId;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;
}
