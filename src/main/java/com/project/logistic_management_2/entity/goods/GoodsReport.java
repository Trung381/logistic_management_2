package com.project.logistic_management_2.entity.goods;


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
public class GoodsReport extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "goods_id")
    private String goodsId;

    @Column(name = "beginning_inventory")
    private Float beginningInventory;

    @Column(name = "ending_inventory")
    private Float endingInventory;
}
