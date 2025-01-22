package com.project.logistic_management_2.entity.transaction;

import com.project.logistic_management_2.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction extends BaseEntity {
    @Id
    private String id;

    @Column(name = "ref_user_id")
    private String refUserId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "goods_id")
    private String goodsId;

    @Column(name = "quantity")
    private Float quantity;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "transaction_time")
    private Date transactionTime;

    @Column(name = "origin")
    private Boolean origin;

    @Column(name = "destination")
    private String destination;

    @Column(name = "image")
    private String image;
}
