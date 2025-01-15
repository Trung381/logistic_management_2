package com.project.logistic_management_2.entity.schedule;

import com.project.logistic_management_2.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schedule_configs")
public class ScheduleConfig extends BaseEntity {
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
}
