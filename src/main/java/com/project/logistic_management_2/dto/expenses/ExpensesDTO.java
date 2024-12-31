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
public class ExpensesDTO {

    @ExportColumn(name = "Mã chi phí")
    private String id;

    //Thong tin tai xe lien quan
    private String driverId;
    @ExportColumn(name = "Tài xế")
    private String driverName;

    //Mã loại chi phí
    @NotBlank(message = "Loại chi phí không được để trống!")
    private String expensesConfigId;
    @ExportColumn(name = "Loại chi phí")
    private String expensesConfigType;

    //Số tiền tương ứng
    @NotNull(message = "Số tiền không được để trống!")
    @ExportColumn(name = "Số tiền")
    private Float amount;
    private Float totalAmount;

    //Ghi chú
    @ExportColumn(name = "Ghi chú")
    private String note;

    //Anh hoa don
    @ExportColumn(name = "Hóa đơn")
    private String imgPath;

    //Mã lịch trình
    @NotBlank(message = "Thông tin lịch trình không được để trống!")
    @ExportColumn(name = "Mã hành trình")
    private String scheduleId;

    private Integer status;

    @ExportColumn(name = "Ngày tạo")
    private Date createdAt;
    private Date updatedAt;


    public ExpensesDTO(String expensesConfigId, String expensesConfigType, Float totalAmount) {
        this.expensesConfigId = expensesConfigId;
        this.expensesConfigType = expensesConfigType;
        this.totalAmount = totalAmount;
    }
}
