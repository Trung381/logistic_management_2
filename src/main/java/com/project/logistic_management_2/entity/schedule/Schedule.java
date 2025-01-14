package com.project.logistic_management_2.entity.schedule;

import com.project.logistic_management_2.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schedules")
public class Schedule extends BaseEntity {
    @Id
    private String id;

    @Column(name = "schedule_config_id")
    private String scheduleConfigId;

    @Column(name = "truck_license")
    private String truckLicense;

    @Column(name = "mooc_license")
    private String moocLicense;

    @Column(name = "departure_time")
    private Date departureTime;

    @Column(name = "arrival_time")
    private Date arrivalTime;

    @Column(name = "note")
    private String note;

    @Column(name = "type")
    private Integer type;

    @Column(name = "status")
    private Integer status;
}
