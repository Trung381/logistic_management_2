package com.project.logistic_management_2.dto.truck;

import com.project.logistic_management_2.annotations.ExportColumn;
import com.project.logistic_management_2.enums.truck.TruckStatus;
import com.project.logistic_management_2.enums.truck.TruckType;
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

    @NotNull(message = "ID tài xế không được để trống")
    private String driverId;
    @ExportColumn(name = "Tài xế")
    private String driverName;

    @NotNull(message = "Loại xe không được để trống")
    private TruckType type;
    @ExportColumn(name = "Loại")
    private String typeDescription;

    private TruckStatus status;
    @ExportColumn(name = "Trạng thái")
    private String statusDescription;

    @ExportColumn(name = "Ghi chú")
    private String note;

    @ExportColumn(name = "Ngày tạo")
    private Date createdAt;
    private Date updatedAt;

    public TruckDTO(int id, String licensePlate, float capacity, String driverId, String driverName, int type, String typeDescription, int status, String statusDescription, String note, Date createdAt, Date updatedAt) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.capacity = capacity;
        this.driverId = driverId;
        this.driverName = driverName;
        this.type = TruckType.valueOf(type);
        this.typeDescription = typeDescription;
        this.status = TruckStatus.valueOf(status);
        this.statusDescription = statusDescription;
        this.note = note;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
