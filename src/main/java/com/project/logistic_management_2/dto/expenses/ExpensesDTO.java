package com.project.logistic_management_2.dto.expenses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpensesDTO {
    private String id;

    //Thong tin tai xe lien quan
    private String driverId;
    private String driverName;

    //Mã lịch trình
    @NotBlank(message = "Thông tin lịch trình không được để trống!")
    private String scheduleId;

    //Mã loại chi phí
    @NotBlank(message = "Loại chi phí không được để trống!")
    private String expensesConfigId;
    private String expensesConfigType;

    //Số tiền tương ứng
    @NotNull(message = "Số tiền không được để trống!")
    private Float amount;

    //Ghi chú
    private String note;

    //Anh hoa don
    private String imgPath;

    private Integer status;

    private Date createdAt;
    private Date updatedAt;

}
