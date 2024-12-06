package com.project.logistic_management_2.dto.request;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

public class TruckDTO {
    @Id
    private String id;
    @Size(min = 8, message = "Biển số xe tối thiểu 8 ký tự")
    private String licensePlate;
    @NotNull(message = "Dung tích xe không được để trống")
    private float capacity;
    @NotNull(message = "ID tài xế không được để trống")
    private String driver_id;
    private String type;
    private Integer deleted; // 0: chưa xóa, 1: đã xóa
    private String note;
}
