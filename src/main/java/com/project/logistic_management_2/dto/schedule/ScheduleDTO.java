package com.project.logistic_management_2.dto.schedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {
    private String id;

    //Thông tin cấu hình lịch trình
    @NotBlank(message = "Cấu hình lịch trình không được để trống!")
    private String scheduleConfigId;
    private String placeA;
    private String placeB;
    private Float amount;

    //Thông tin tài xế + xe, mooc
    @NotBlank(message = "Thông tin tài xế không được để trống!")
    private String driverId;
    private String driverName;
    private String truckLicense;
    private String moocLicense;

    //Thời gian lấy / giao hàng
    @NotNull(message = "Thời gian lấy hàng không được để trống!")
    private Date departureTime;
    @NotNull(message = "Thời gian giao hàng không được để trống!")
    private Date arrivalTime;

    //Ghi chú
    private String note;
    //Ảnh đính kèm
    private String attachDocument;
    //Trạng thái
    private Integer status;

    private Date createdAt;
    private Date updatedAt;
}
