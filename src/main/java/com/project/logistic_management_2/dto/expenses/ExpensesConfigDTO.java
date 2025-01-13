package com.project.logistic_management_2.dto.expenses;

import com.project.logistic_management_2.annotations.ExportColumn;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpensesConfigDTO {

    @ExportColumn(name = "Mã loại chi phí")
    private String id;

    @NotBlank(message = "Loại chi phí không được để trống!")
    @ExportColumn(name = "Loại chi phí")
    private String type;

    @ExportColumn(name = "Ghi chú")
    private String note;

    private Date createdAt;
    private Date updatedAt;
}
