package com.project.logistic_management_2.dto.request;

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
public class TransactionDTO {

    private String id;

    @NotBlank(message = "ID người kiểm duyệt không được để trống")
    private String refUserId;
    private String fullNameUser;

    private String customerName;

    @NotBlank(message = "ID hàng hóa không được để trống")
    private String goodsId;
    private String goodsName;

    @NotNull(message = "Số lượng hàng hóa không được để trống")
    private Float quantity;

    private Date transactionTime;

    @NotNull(message = "Loại giao dịch không được để trống")
    private Boolean origin;
    private String originDescription;

    private String destination;

    private String image;

    private Date createdAt;

    private Date updatedAt;


}


