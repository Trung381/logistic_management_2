package com.project.logistic_management_2.dto.expenses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpensesReportDTO {
    //Thong tin tai xe
    private String driverId;
    private String driverName;

    //Thong tin xe
    private String truckLicense;
    private String moocLicense;

    //Du dau ky (Du cuoi ky cua chu ky truoc)
    private Float prevRemainingBalance;

    //Ung trong ky
    private Float advance;

    //Cac chi phi phat sinh
//    private ExpensesIncurredDTO expensesIncurred;
    private List<ExpensesIncurredDTO> expensesIncurred;

    //Du cuoi ky
    private Float remainingBalance;

    public ExpensesReportDTO(String driverId, String driverName, String truckLicense, String moocLicense, Float prevRemainingBalance, Float advance, Float remainingBalance) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.truckLicense = truckLicense;
        this.moocLicense = moocLicense;
        this.prevRemainingBalance = prevRemainingBalance;
        this.advance = advance;
        this.expensesIncurred = null;
        this.remainingBalance = remainingBalance;
    }
}
