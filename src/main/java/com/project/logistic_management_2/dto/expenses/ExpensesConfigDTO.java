package com.project.logistic_management_2.dto.expenses;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpensesConfigDTO {
    private String id;

    //loại chi phí
    @NotBlank(message = "Loại chi phí không được để trống!")
    private String type;
    //ghi chú
    private String note;

    private Date createdAt;
    private Date updatedAt;
}
