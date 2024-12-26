package com.project.logistic_management_2.dto.truck;

import com.project.logistic_management_2.annotations.ExportColumn;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TruckDTO {
    @ExportColumn(name = "Mã")
    private Integer id;

    @Size(min = 8, message = "Biển số xe tối thiểu 8 ký tự")
    @ExportColumn(name = "Biến số")
    private String licensePlate;

    @NotNull(message = "Dung tích xe không được để trống")
    @ExportColumn(name = "Dung tích")
    private float capacity;

    //Thong tin tai xe
    @NotNull(message = "ID tài xế không được để trống")
    private String driverId;
    @ExportColumn(name = "Tài xế")
    private String driverName;

    @NotNull(message = "Loại xe không được để trống")
    @Min(value = 1, message = "Loại xe chỉ có thể là 1 hoặc 2")
    @Max(value = 2, message = "Loại xe chỉ có thể là 1 hoặc 2")
    private Integer type; //loai xe: 0 - xe tai, 1 - mooc
    @ExportColumn(name = "Loại")
    private String typeDescription;

    private Integer status; //Trang thai xe
    @ExportColumn(name = "Trạng thái")
    private String statusDescription;

    @ExportColumn(name = "Ghi chú")
    private String note;

    @ExportColumn(name = "Ngày tạo")
    private Date createdAt;
    private Date updatedAt;
}
