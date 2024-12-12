package com.project.logistic_management_2.dto.request;

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
    private String id;

    @NotBlank(message = "Tên kho không được để trống")
    private String name;

    private String note;

    private Date createdAt;

    private Date updatedAt;
}
