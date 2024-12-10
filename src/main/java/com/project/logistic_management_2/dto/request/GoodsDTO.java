package com.project.logistic_management_2.dto.request;

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
    private String id;

    @NotBlank(message = "ID kho không được để trống")
    private String warehouseId;

    @NotBlank(message = "Tên hàng hóa không được để trống")
    private String name;

    @NotNull(message = "Số lượng hàng hóa không được để trống")
    private Float quantity;

    @NotNull(message = "Giá tiền hàng hóa không được để trống")
    private Float amount;

    private Date createdAt;
    private Date updatedAt;
}
