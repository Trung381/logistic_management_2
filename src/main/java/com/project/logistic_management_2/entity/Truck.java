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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "driver_id", nullable = false)
    private String driverId;

    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Column(name = "capacity", nullable = false)
    private Float capacity;

    @Column(name = "note")
    private String note;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Column(name = "status", nullable = false)
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
