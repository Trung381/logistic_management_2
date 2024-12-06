package com.project.logistic_management_2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trucks")
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

    /*
    Trạng thái của xe:
    -1: Bảo trì
     0: Không có sẵn
     1: Có sẵn (Mặc định)
    */
    @Column(name = "status", nullable = false, columnDefinition = "INT DEFAULT 1")
    private Integer status;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updatedAt;
}
