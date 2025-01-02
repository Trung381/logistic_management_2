package com.project.logistic_management_2.dto.transaction;

import com.project.logistic_management_2.annotations.ExportColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionDTO {
    private String refUserId;
    private String customerName;
    private String goodsId;
    private Float quantity;
    private Date transactionTime;
    private Boolean origin;
    private String destination;
    private String image;
    private String fullNameRefUser;
    private String goodsName;
    private String originDescription;

    private Date updatedAt;
}