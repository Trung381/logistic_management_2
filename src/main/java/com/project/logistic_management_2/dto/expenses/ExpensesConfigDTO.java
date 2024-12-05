package com.project.logistic_management_2.dto.expenses;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExpensesConfigDTO {
    //loại chi phí
    @NotBlank(message = "Loại chi phí không được để trống!")
    private String type;
    //ghi chú
    private String note;
}
