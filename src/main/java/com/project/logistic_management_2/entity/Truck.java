package com.project.logistic_management_2.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "trucks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Truck {
    @Id
    private String id;
    @Column(name = "driver_id", nullable = false)
    private String driverId;
    @Column(name = "license_plate", nullable = false)
    private String licensePlate;
    @Column(name = "capacity", nullable = false, columnDefinition = "FLOAT DEFAULT 0")
    private Float capacity;
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
    @Column(name = "deleted", nullable = false)
    private Integer deleted; //1 là chưa xóa, 0 là đã xóa

    /*
    Trạng thái của xe:
    -1: Bảo trì
     0: Không có sẵn
     1: Có sẵn (Mặc định)
    */
    @Column(name = "status", nullable = false, columnDefinition = "INT DEFAULT 1")
    private Integer status;

    @Column(name = "type", nullable = false)
    private Integer type;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updatedAt;

}
