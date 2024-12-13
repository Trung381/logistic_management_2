package com.project.logistic_management_2.dto.expenses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Date;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseAdvancesDTO {
    private Integer id;

    @NotBlank(message = "Thông tin tài xế không được để trống!")
    private String driverId;

    private String driverName;

    @NotBlank(message = "Chu kỳ ứng chi phí không được để trống!")
    @Pattern(regexp = "^(\\d{4}-(0[1-9]|1[0-2]))$", message = "Định dạng chu kỳ không hợp lệ! Dạng đúng: yyyy-MM")
    private String period;

    @NotNull(message = "Tiền ứng không được để trống!")
    private Float advance;

    private Float remainingBalance;

    private String note;

    private Date createdAt;
    private Date updatedAt;
}
