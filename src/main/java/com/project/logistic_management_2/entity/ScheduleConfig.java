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
@Table(name = "schedule_configs")
public class ScheduleConfig {
    @Id
    private String id;

    @Column(name = "place_a")
    private String placeA;

    @Column(name = "place_b")
    private String placeB;

    @Column(name = "amount")
    private Float amount;

    @Column(name = "note")
    private String note;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;
}
