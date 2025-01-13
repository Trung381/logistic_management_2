package com.project.logistic_management_2.entity.salary;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "salary")
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "period")
    private String period;

    // Các trường từ salary_received
    @Column(name = "phone_allowance")
    private Float phoneAllowance;

    @Column(name = "basic_salary")
    private Float basicSalary;

    @Column(name = "job_allowance")
    private Float jobAllowance;

    @Column(name = "bonus")
    private Float bonus;

    @Column(name = "monthly_paid_leave")
    private Float monthlyPaidLeave;

    @Column(name = "ot")
    private Float ot;

    @Column(name = "received_snn")
    private Float receivedSnn;

    @Column(name = "union_contribution")
    private Float unionContribution;

    @Column(name = "travel_expenses_reimbursement")
    private Float travelExpensesReimbursement;

    // Các trường từ salary_deduction
    @Column(name = "mandatory_insurance")
    private Float mandatoryInsurance;

    @Column(name = "trade_union")
    private Float tradeUnion;

    @Column(name = "advance")
    private Float advance;

    @Column(name = "error_of_driver")
    private Float errorOfDriver;

    @Column(name = "deduction_snn")
    private Float deductionSnn;
}
