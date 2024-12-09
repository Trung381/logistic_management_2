package com.project.logistic_management_2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "salary_received")
public class SalaryReceived {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "phone_allowance")
    private Float phoneAllowance;

    @Column(name = "basic_salary")
    private Float basicSalary;

    @Column(name = "period")
    private String period;

    @Column(name = "job_allowance")
    private Float jobAllowance;

    @Column(name = "bonus")
    private Float bonus;

    @Column(name = "monthly_paid_leave")
    private Float monthlyPaidLeave;

    @Column(name = "ot")
    private Float ot;

    @Column(name = "snn")
    private Float snn;

    @Column(name = "union_contribution")
    private Float unionContribution;

    @Column(name = "travel_expenses_reimbursement")
    private Float travelExpensesReimbursement;
}
