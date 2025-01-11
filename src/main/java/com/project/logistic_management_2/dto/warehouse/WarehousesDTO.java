package com.project.logistic_management_2.dto.warehouse;

import com.project.logistic_management_2.annotations.ExportColumn;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehousesDTO {

    @ExportColumn(name = "Mã kho")
    private String id;

    @NotBlank(message = "Tên kho không được để trống")
    @ExportColumn(name = "Tên kho")
    private String name;

    @ExportColumn(name = "Ghi chú")
    private String note;

    @ExportColumn(name = "Ngày tạo")
    private Date createdAt;

    @ExportColumn(name = "Ngày cập nhật")
    private Date updatedAt;
}
