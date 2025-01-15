package com.project.logistic_management_2.entity.expenses;

import com.project.logistic_management_2.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expense_advances")
public class ExpenseAdvances extends BaseEntity {
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
}
