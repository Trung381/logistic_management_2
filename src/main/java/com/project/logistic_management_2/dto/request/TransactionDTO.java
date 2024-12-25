package com.project.logistic_management_2.dto.request;

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
public class TransactionDTO {

    private String id;

    @NotBlank(message = "ID người kiểm duyệt không được để trống")
    private String refUserId;
    private String fullNameRefUser;

    @NotBlank(message = "ID hàng hóa không được để trống")
    @ExportColumn(name = "Mã hàng hóa")
    private String goodsId;
    @ExportColumn(name = "Tên hàng hóa")
    private String goodsName;

    @NotNull(message = "Số lượng hàng hóa không được để trống")
    @ExportColumn(name = "KL (Tấn)")
    private Float quantity;

    @ExportColumn(name = "Ghi chú")
    private String destination;

    @ExportColumn(name = "Khách hàng")
    private String customerName;

    @ExportColumn(name = "Ngày giao dịch")
    private Date transactionTime;

    @NotNull(message = "Loại giao dịch không được để trống")
    private Boolean origin;
    @ExportColumn(name = "Loại giao dịch")
    private String originDescription;

    private String image;

    private Date createdAt;

    private Date updatedAt;


}


