package com.project.logistic_management_2.dto.schedule;

import com.project.logistic_management_2.annotations.ExportColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDTO {
    @ExportColumn(name = "Mã")
    private String id;

    //Thông tin cấu hình lịch trình, null nếu chạy nội bộ
//    @NotBlank(message = "Cấu hình lịch trình không được để trống!")
    private String scheduleConfigId;
    @ExportColumn(name = "Điểm đi")
    private String placeA;
    @ExportColumn(name = "Điểm đến")
    private String placeB;
    @ExportColumn(name = "Số tiền")
    private Float amount;

    //Thông tin tài xế + xe, mooc
    @NotBlank(message = "Thông tin tài xế không được để trống!")
    @ExportColumn(name = "Mã tài xế")
    private String driverId;

    @ExportColumn(name = "Tên tài xế")
    private String driverName;

    @NotBlank(message = "Thông tin xe tải không được để trống!")
    @ExportColumn(name = "BS Xe")
    private String truckLicense;

    @NotBlank(message = "Thông tin rơ-mooc không được để trống!")
    private String moocLicense;

    //Thời gian lấy / giao hàng
//    @NotNull(message = "Thời gian lấy hàng không được để trống!")
    @ExportColumn(name = "Ngày lấy hàng")
    private Date departureTime;

//    @NotNull(message = "Thời gian giao hàng không được để trống!")
    @ExportColumn(name = "Ngày giao hàng")
    private Date arrivalTime;

    //Ghi chú
    @ExportColumn(name = "Ghi chú")
    private String note;
    //Ảnh đính kèm
    private String attachDocument;
    //Loại hành trình: 0 - nội bộ, 1 - tính lương
    @NotNull(message = "Loại hành trình không được để trống")
    private Integer type;
    //Trạng thái
    private Integer status;

    private Integer count = 1; //Số chuyến phát sinh: mặc định = 1

    @ExportColumn(name = "Ngày tạo")
    private Date createdAt;
    private Date updatedAt;

    public ScheduleDTO(String id, String scheduleConfigId, String placeA, String placeB, Float amount, String driverId, String driverName, String truckLicense, String moocLicense, Date departureTime, Date arrivalTime, String note, String attachDocument, Integer type, Integer status, Date createdAt, Date updatedAt) {
        this.id = id;
        this.scheduleConfigId = scheduleConfigId;
        this.placeA = placeA;
        this.placeB = placeB;
        this.amount = amount;
        this.driverId = driverId;
        this.driverName = driverName;
        this.truckLicense = truckLicense;
        this.moocLicense = moocLicense;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.note = note;
        this.attachDocument = attachDocument;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ScheduleDTO(String scheduleConfigId, String placeA, String placeB, String driverId, String driverName, String truckLicense, String moocLicense, Date departureTime, Date arrivalTime, Integer count) {
        this.scheduleConfigId = scheduleConfigId;
        this.placeA = placeA;
        this.placeB = placeB;
        this.driverId = driverId;
        this.driverName = driverName;
        this.truckLicense = truckLicense;
        this.moocLicense = moocLicense;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.count = count;
    }
}
