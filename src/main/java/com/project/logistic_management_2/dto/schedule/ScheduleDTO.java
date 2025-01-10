package com.project.logistic_management_2.dto.schedule;

import com.project.logistic_management_2.annotations.ExportColumn;
import com.project.logistic_management_2.enums.schedule.ScheduleStatus;
import com.project.logistic_management_2.enums.schedule.ScheduleType;
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
    private String scheduleConfigId;
    @ExportColumn(name = "Điểm đi")
    private String placeA;
    @ExportColumn(name = "Điểm đến")
    private String placeB;
    @ExportColumn(name = "Số tiền")
    private Float amount;

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

    //Thời gian khởi hành/hoàn thành
    @ExportColumn(name = "Ngày lấy hàng")
    private Date departureTime;
    @ExportColumn(name = "Ngày giao hàng")
    private Date arrivalTime;

    @ExportColumn(name = "Ghi chú")
    private String note;

    //Ảnh đính kèm
    private String attachedPaths;

    //Loại hành trình: nội bộ/tính lương
    @NotNull(message = "Loại hành trình không được để trống")
    private ScheduleType type;
    //Trạng thái: không duyệt/đang chờ/duyệt + chưa hoàn thành/đã hoàn thành
    private ScheduleStatus status;

    private Integer count = 1; //Số chuyến phát sinh: mặc định = 1

    @ExportColumn(name = "Ngày tạo")
    private Date createdAt;
    private Date updatedAt;

    public ScheduleDTO(String id, String scheduleConfigId, String placeA, String placeB, Float amount, String driverId, String driverName, String truckLicense, String moocLicense, Date departureTime, Date arrivalTime, String note, String attachedPaths, Integer type, Integer status, Date createdAt, Date updatedAt) {
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
        this.attachedPaths = attachedPaths;
        this.type = ScheduleType.valueOf(type);
        this.status = ScheduleStatus.valueOf(status);
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
