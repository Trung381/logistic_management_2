package com.project.logistic_management_2.dto.request;

import com.project.logistic_management_2.annotations.ExportColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDTO {

    @ExportColumn(name = "Mã hàng hóa")
    private String id;

    @NotBlank(message = "ID kho không được để trống")
    @ExportColumn(name = "Mã kho")
    private String warehouseId;
    @ExportColumn(name = "Tên kho")
    private String warehouseName;

    @NotBlank(message = "Tên hàng hóa không được để trống")
    @ExportColumn(name = "Tên hàng hóa")
    private String name;

    @NotNull(message = "Số lượng hàng hóa không được để trống")
    @ExportColumn(name = "Số lượng hàng hóa")
    private Float quantity;

    @NotNull(message = "Giá tiền hàng hóa không được để trống")
    @ExportColumn(name = "Đơn giá hàng hóa")
    private Float amount;

    @ExportColumn(name = "Ngày tạo")
    private Date createdAt;

    @ExportColumn(name = "Ngày sửa")
    private Date updatedAt;
}
