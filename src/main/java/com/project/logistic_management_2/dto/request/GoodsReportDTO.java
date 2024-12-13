package com.project.logistic_management_2.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReportDTO {

    private Long id;

    @NotBlank(message = "ID hàng hóa không được để trống")
    private String goodsId;
    private String goodsName;

    private Float beginingInventoryQuantity;
    private Float beginingInventoryTotalAmount;

    private Float endingInventoryQuantity;
    private Float endingInventoryTotalAmount;

    private Float inboundTransactionQuantity;
    private Float inboundTransactionTotalAmount;

    private Float outboundTransactionQuantity;
    private Float outboundTransactionTotalAmount;

    private Float unitAmount;

    private Date createdAt;
}
