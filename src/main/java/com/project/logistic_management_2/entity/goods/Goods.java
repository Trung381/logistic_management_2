package com.project.logistic_management_2.entity.goods;

import com.project.logistic_management_2.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "goods")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Goods extends BaseEntity {
    @Id
    private String id;

    @Column(name = "warehouse_id")
    private String warehouseId;

    @Column(name = "name")
    private String name;

    @Column(name = "quantity")
    private Float quantity;

    @Column(name = "amount")
    private Float amount;
}
