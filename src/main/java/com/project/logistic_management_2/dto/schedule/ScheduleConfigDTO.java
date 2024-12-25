package com.project.logistic_management_2.dto.schedule;


import com.project.logistic_management_2.annotations.ExportColumn;
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

    @ExportColumn(name = "Mã HT")
    private String id;

    @NotBlank(message = "Thông tin địa điểm không được để trống!")
    @ExportColumn(name = "Điểm đi")
    private String placeA;
    @NotBlank(message = "Thông tin địa điểm không được để trống!")
    @ExportColumn(name = "Điểm đến")
    private String placeB;

    @NotNull(message = "Chi phí lịch trình không được để trống!")
    @ExportColumn(name = "Giá chuyến")
    private Float amount;

    @ExportColumn(name = "Ghi chú")
    private String note;

    private Date createdAt;
    private Date updatedAt;
}
