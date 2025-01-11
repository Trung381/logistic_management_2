package com.project.logistic_management_2.dto.salary;

import com.project.logistic_management_2.annotations.ExportColumn;
import lombok.*;

@Getter
@Setter
@Builder
public class SalaryDTO {
    @ExportColumn(name = "Phụ cấp điện thoại")
    private Float phoneAllowance;
    @ExportColumn(name = "Lương cơ bản")
    private Float basicSalary;
    @ExportColumn(name = "Phụ cấp công việc")
    private Float jobAllowance;
    @ExportColumn(name = "Thưởng")
    private Float bonus;
    @ExportColumn(name = "Lương ngày nghỉ trong tháng")
    private Float monthlyPaidLeave;
    @ExportColumn(name = "Lương đi làm ngày nghỉ Lễ/Tết")
    private Float ot;
    @ExportColumn(name = "Bảo hiểm chi trả ốm đau/thai sản")
    private Float receivedSnn;
    @ExportColumn(name = "Công đoàn công ty")
    private Float unionContribution;
    @ExportColumn(name = "Thanh toán tiền đi đường")
    private Float travelExpensesReimbursement;
    @ExportColumn(name = "Bảo hiểm bắt buộc (10,5%)")
    private Float mandatoryInsurance;
    @ExportColumn(name = "Kinh phí công đoàn (1%)")
    private Float tradeUnion;
    @ExportColumn(name = "Tổng tạm ứng lương")
    private Float advance;
    @ExportColumn(name = "Trừ phạt tiền do lỗi tài xế")
    private Float errorOfDriver;
    @ExportColumn(name = "Truy thu BHYT/BHXH")
    private Float deductionSnn;
}
