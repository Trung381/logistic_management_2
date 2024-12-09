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
public class ScheduleConfigDTO {
    private String id;

    @NotBlank(message = "Thông tin địa điểm không được để trống!")
    private String placeA;
    @NotBlank(message = "Thông tin địa điểm không được để trống!")
    private String placeB;

    @NotNull(message = "Chi phí lịch trình không được để trống!")
    private Float amount;

    private String note;

    private Date createdAt;
    private Date updatedAt;
}
