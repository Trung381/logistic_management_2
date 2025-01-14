package com.project.logistic_management_2.entity.goods;

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
public class Goods {
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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;
}
