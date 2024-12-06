package com.project.logistic_management_2.dto.expenses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExpensesDTO {
    //Mã lịch trình
    @NotBlank(message = "Thông tin lịch trình không được để trống!")
    private String scheduleId;
    //Mã loại chi phí
    @NotBlank(message = "Loại chi phí không được để trống!")
    private String expensesConfigId;
    //Số tiền tương ứng
    @NotNull(message = "Số tiền không được để trống!")
    private Float amount;
    //Ghi chú
    private String note;
}
