package com.project.logistic_management_2.dto.salarydeduction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalaryDeductionDTO {
    private Float mandatoryInsurance;
    private Float tradeUnion;
    private Float advance;
    private Float errorOfDriver;
    private Float snn;
}
