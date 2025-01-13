package com.project.logistic_management_2.dto.schedule;

import com.project.logistic_management_2.annotations.ExportColumn;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class ScheduleSalaryDTO {
    @ExportColumn(name = "Điểm đi")
    private String placeA;
    @ExportColumn(name = "Điểm đến")
    private String placeB;
    @ExportColumn(name = "Đơn giá")
    private Float amount; //Gia tien 1 chuyen
    @ExportColumn(name = "Số chuyến")
    private Integer count;
    @ExportColumn(name = "Thành tiền")
    private Float total;

    public ScheduleSalaryDTO(String driverName, String placeA, String placeB, Float amount, Integer count, Float total) {
        //this.driverName = driverName;
        this.placeA = placeA;
        this.placeB = placeB;
        this.amount = amount;
        this.count = count;
        this.total = total;
    }

    public ScheduleSalaryDTO(String placeA, String placeB, Float amount, Integer count, Float total) {
        //this.driverName = driverName;
        this.placeA = placeA;
        this.placeB = placeB;
        this.amount = amount;
        this.count = count;
        this.total = total;
    }
}
