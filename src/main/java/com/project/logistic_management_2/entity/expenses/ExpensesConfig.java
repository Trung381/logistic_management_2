package com.project.logistic_management_2.entity.expenses;

import com.project.logistic_management_2.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "expenses_configs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpensesConfig extends BaseEntity {
    @Id
    private String id;

    @Column(name = "type")
    private String type;

    @Column(name = "note")
    private String note;
}
