package com.project.logistic_management_2.dto.truck;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private Integer id;
    @Size(min = 8, message = "Biển số xe tối thiểu 8 ký tự")
    private String licensePlate;
    @NotNull(message = "Dung tích xe không được để trống")
    private float capacity;

    //Thong tin tai xe
    @NotNull(message = "ID tài xế không được để trống")
    private String driverId;
    private String driverName;

    @NotNull(message = "Loại xe không được để trống")
    private Integer type; //loai xe: 0 - xe tai, 1 - mooc
    private Integer status; //Trang thai xe
    private String note;
    private Date createdAt;
    private Date updatedAt;
}
