package com.project.logistic_management_2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Expenses {
    @Id
    private String id;

    @Column(name = "schedule_id")
    private String scheduleId;

    @Column(name = "expenses_config_id")
    private String expensesConfigId;

    @Column(name = "amount")
    private Float amount;

    @Column(name = "note")
    private String note;

    /* Trạng thái chi phí
    0: Chưa duyệt (Mặc định)
    1: Đã duyệt
    */
    @Column(name = "status")
    private Integer status;

    @Column(name = "created_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updatedAt;
}
