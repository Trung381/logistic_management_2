package com.project.logistic_management_2.entity.warehouse;

import com.project.logistic_management_2.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Warehouses extends BaseEntity {
    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "note")
    private String note;
}
