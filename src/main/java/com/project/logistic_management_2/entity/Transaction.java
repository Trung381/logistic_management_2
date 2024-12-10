package com.project.logistic_management_2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    private String id;

    @Column(name = "ref_user_id")
    private String refUserId;

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

    @Column(name = "deleted")
    private Boolean deleted;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;
}
