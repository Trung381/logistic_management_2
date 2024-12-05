package com.project.logistic_management_2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "expenses_configs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpensesConfig {
    @Id
    private String id;

    @Column(name = "type")
    private String type;

    @Column(name = "note")
    private String note;

    @Column(name = "created_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updatedAt;
}
