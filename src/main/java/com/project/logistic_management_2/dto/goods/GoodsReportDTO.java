package com.project.logistic_management_2.dto.goods;

import com.project.logistic_management_2.annotations.ExportColumn;
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
    @ExportColumn(name = "Mã hiệu")
    private String goodsId;
    @ExportColumn(name = "Tên VTHH")
    private String goodsName;

    @ExportColumn(name = "Số lượng tồn đầu kỳ")
    private Float beginingInventoryQuantity;
    @ExportColumn(name = "Giá trị tồn đầu kỳ")
    private Float beginingInventoryTotalAmount;

    @ExportColumn(name = "Số lượng nhập trong kỳ")
    private Float inboundTransactionQuantity;
    @ExportColumn(name = "Giá trị nhập trong kỳ")
    private Float inboundTransactionTotalAmount;

    @ExportColumn(name = "Số lượng xuất trong kỳ")
    private Float outboundTransactionQuantity;
    @ExportColumn(name = "Giá trị xuất trong kỳ")
    private Float outboundTransactionTotalAmount;

    @ExportColumn(name = "Số lượng tồn cuối kỳ")
    private Float endingInventoryQuantity;
    @ExportColumn(name = "Giá trị tồn cuối kỳ")
    private Float endingInventoryTotalAmount;

    @ExportColumn(name = "Đơn giá")
    private Float unitAmount;

    private Date createdAt;
}
