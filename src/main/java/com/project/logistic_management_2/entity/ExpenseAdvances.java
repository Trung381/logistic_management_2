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
@Table(name = "expense_advances")
public class ExpenseAdvances {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "driver_id")
    private String driverId;

    @Column(name = "period")
    private String period;

    @Column(name = "advance")
    private Float advance;

    @Column(name = "remaining_balance")
    private Float remainingBalance;

    @Column(name = "note")
    private String note;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "created_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updatedAt;
}
