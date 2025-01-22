package com.project.logistic_management_2.entity.truck;

import com.project.logistic_management_2.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trucks")
public class Truck extends BaseEntity {
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

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "type", nullable = false)
    private Integer type;
}
