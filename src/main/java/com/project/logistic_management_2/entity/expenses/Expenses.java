package com.project.logistic_management_2.entity.expenses;

import com.project.logistic_management_2.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Expenses extends BaseEntity {
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

    @Column(name = "status")
    private Integer status;
}
