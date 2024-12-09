package com.project.logistic_management_2.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "salary_deduction")
public class SalaryDeduction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "mandatory_insurance")
    private Float mandatoryInsurance;

    @Column(name = "trade_union")
    private Float tradeUnion;

    @Column(name = "advance")
    private Float advance;

    @Column(name = "error_of_driver")
    private Float errorOfDriver;

    @Column(name = "snn")
    private Float snn;

    @Column(name = "period")
    private String period;

}
