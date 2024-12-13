package com.project.logistic_management_2.dto.schedule;

import lombok.Data;

@Data
public class ScheduleSalaryDTO {
    //thong tin tai xe
//    private String driverId;
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
//package com.project.logistic_management_2.dto.schedule;
//
//import lombok.Data;
//
//@Data
//public class ScheduleSalaryDTO {
//    // Thông tin tài xế
//    private String driverId;
//    private String driverName;
//
//    // Thông tin lịch trình
//    private String placeA;
//    private String placeB;
//    private Float amount; // Giá tiền 1 chuyến
//    private Integer count;
//    private Float total;
//
//    // Cập nhật constructor để bao gồm driverId
//    public ScheduleSalaryDTO(String driverId, String driverName, String placeA, String placeB, Float amount, Integer count, Float total) {
//        this.driverId = driverId;
//        this.driverName = driverName;
//        this.placeA = placeA;
//        this.placeB = placeB;
//        this.amount = amount;
//        this.count = count;
//        this.total = total;
//    }
//}
