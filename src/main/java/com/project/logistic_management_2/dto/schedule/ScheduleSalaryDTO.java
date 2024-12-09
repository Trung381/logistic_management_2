package com.project.logistic_management_2.dto.schedule;

import lombok.Data;

@Data
public class ScheduleSalaryDTO {
    //thong tin tai xe
    private String driverId;
    private String driverName;

    //thong tin lich trinh
    private String placeA;
    private String placeB;
    private Float amount; //Gia tien 1 chuyen
    private Integer count;
    private Float total;

    public ScheduleSalaryDTO(String driverName, String placeA, String placeB, Float amount, Integer count, Float total) {
        this.driverName = driverName;
        this.placeA = placeA;
        this.placeB = placeB;
        this.amount = amount;
        this.count = count;
        this.total = total;
    }
}
